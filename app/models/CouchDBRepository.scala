package models

import models.traits.{CouchBaseModel, CouchDBRepositoryBase}

trait CouchDBRepository[A <: CouchBaseModel] extends CouchDBRepositoryBase[A]{
  override lazy val database = CouchDBConnection.database
  override lazy val design = CouchDBConnection.design
}