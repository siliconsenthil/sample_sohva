package models.traits

import java.util.UUID._
import scala.Some
import scala.reflect.runtime.{universe => ru}
import utils.Tapper._
import utils.CouchSyncRunner._
import gnieh.sohva.async.{Design, Database}

trait CouchDBRepositoryBase[A <: CouchBaseModel] extends BaseRepo[A] {
  val database: Database
  val design: Design

  def createOrUpdate(entity: A)(implicit manifest: Manifest[A]): A = {
    findById(entity._id) match {
      case Some(e) => this.update(entity)
      case None => this.create(entity)
    }
  }

  override def create(entity: A)(implicit manifest: Manifest[A]): A = {
    entity.beforeValidation()
    if (entity.isValid) {
      if (entity._id == null) {
        setField(entity, "_id", randomUUID.toString)
      }
      database.saveDoc(entity.tap(_.beforeCreate())).sync
      entity.tap(_.afterCreate()).tap(_.afterSave())
    } else entity
  }

  override def update(entity: A)(implicit manifest: Manifest[A]) = {
    val rev = findById(entity._id).get._rev
    entity.beforeValidation()
    entity.beforeUpdate()
    if (entity.isValid) {
      database.saveDoc(entity.withRev(rev)).sync.get
      entity.tap(_.afterSave())
    } else entity
  }

  override def destroy(id: String)(implicit manifest: Manifest[A]) = database.deleteDoc(id).sync

  override def findById(id: String)(implicit manifest: Manifest[A]): Option[A] = {
    Option(id).flatMap {
      idValue =>
        database.getDocById[A](idValue).sync.map(entity => entity.tap(_.afterFind()))
    }
  }

  override def getById(id: String)(implicit manifest: Manifest[A]) = findById(id).get

  def list(implicit manifest: Manifest[A]) = findAllBy("byType", manifest.runtimeClass.getSimpleName)

  def findAllBy(viewName: String, value: String)(implicit manifest: Manifest[A]) = {
    design.view[String, A, A](viewName).query(Some(value)).sync.rows.map(_.value)
  }

  def findAllSortedBy(sortedViewName: String, value: String)(implicit manifest: Manifest[A]) = {
    design.view[List[String], A, A](sortedViewName).query(
      startkey = Some(List(value)),
      endkey = Some(List(value, "\ufff0"))
    ).sync.rows.map(_.value)
  }

  private[models] def setField(entity: A, fieldName: String, fieldValue: Any)(implicit manifest: Manifest[A]) {
    val m = ru.runtimeMirror(entity.getClass.getClassLoader)
    val idTermSymbol = ru.typeOf[CouchBaseModel].declaration(ru.newTermName(fieldName)).asTerm
    val im = m.reflect(entity)
    val idFieldMirror = im.reflectField(idTermSymbol)
    idFieldMirror.set(fieldValue)
  }
}
