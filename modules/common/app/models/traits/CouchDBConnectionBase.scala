package models.traits

import gnieh.sohva.async.CouchClient
import gnieh.sohva.JsonSerializer
import net.liftweb.json._
import org.joda.time.DateTime
import utils.JsonUtils
import net.liftweb.json.FieldSerializer._
import utils.Tapper._
import net.liftweb.json.TypeInfo

trait   CouchDBConnectionBase {
  val username: String
  val password: String
  val dbName: String

  val client = new CouchClient() {
    private val existingFormats: Formats = new JsonSerializer(version, List()).formats

    object DateTimeSerializer extends Serializer[DateTime] {
      private val DateTimeClass = classOf[DateTime]

      def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DateTime] = {
        case (TypeInfo(DateTimeClass, _), json) => JsonUtils.CustomDateMapper.fromJson(json.values.asInstanceOf[String])
      }

      def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
        case x: DateTime => JString(JsonUtils.CustomDateMapper.toJson(x))
      }
    }

    override val serializer: JsonSerializer = new JsonSerializer(version, List()) {
      def ignoreFields: PartialFunction[(String, Any), Option[(String, Any)]] = {
        case (name, value) =>
          if (name.contains("$") || name.equals("id")) None else Some(name, value)
      }

      override implicit val formats: Formats = {
        existingFormats +
          FieldSerializer[CouchBaseModel](ignoreFields) +
          FieldSerializer[CouchNestedModel](ignoreFields) +
          DateTimeSerializer
      }
    }
  }

  lazy val session = client.startCookieSession.tap(_.login(username, password))
  lazy val database = session.database(dbName).tap(_.create)
  lazy val design = database.design("views", "javascript")

  def refreshViews()
}
