package models.traits

import gnieh.sohva.IdRev
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(Array("_rev"))
abstract class CouchBaseModel(id: Option[String] = None) extends BaseModel with IdRev {
  @JsonProperty("id")
  override val _id = id.getOrElse(null)

  val docType = getClass.getSimpleName

  def beforeValidation() {}
  def beforeCreate() {}
  def beforeUpdate() {}
  def afterCreate() {}
  def afterSave() {}
  def afterFind() {}

  @JsonIgnore
  def isNewDoc: Boolean = _id == null

  protected def myself: (IdRev) => Boolean = d => d._id == _id
}