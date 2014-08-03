package models

import models.traits.CouchBaseModel

case class Employee(name: String) extends CouchBaseModel {
  lazy val decoratedName = "name: " + name
}

object Employee extends CouchDBRepository[Employee]
