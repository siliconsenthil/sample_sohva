package models.traits

abstract class BaseModel extends Validation

trait BaseRepo[A <: BaseModel] {
  def create(a: A)(implicit manifest: Manifest[A]): A
  def update(a: A)(implicit manifest: Manifest[A]): A
  def findById(id: String)(implicit manifest: Manifest[A]): Option[A]
  def getById(id: String)(implicit manifest: Manifest[A]): A
  def destroy(id: String)(implicit manifest: Manifest[A]): Boolean
}
