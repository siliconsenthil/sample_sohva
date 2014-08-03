package models.traits
import java.util.UUID._
import com.fasterxml.jackson.annotation.JsonProperty

abstract class CouchNestedModel(private val nestedModelId: Option[String] = None) extends Validation {
   @JsonProperty("id")
   val _id = nestedModelId.getOrElse(randomUUID.toString)
 }
