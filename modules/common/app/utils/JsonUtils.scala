package utils

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.core.{JsonParser, JsonGenerator}
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import play.api.Logger

object JsonUtils {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.setSerializationInclusion(Include.NON_NULL)
  mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  mapper.registerModule(DefaultScalaModule)

  mapper.registerModule(CustomDateMapper.module)

  def toJson(obj: Any) = {
    mapper.writeValueAsString(obj)
  }

  def fromJson[T: Manifest](json: String): T = {
    mapper.readValue[T](json)
  }

  object CustomDateMapper {
    private val Format = "dd/MM/yyyy"

    def module = {
      val formatterModule = new SimpleModule("CustomDateFormatterModule")
      formatterModule.addSerializer(classOf[DateTime], CustomDateMapper.serializer)
      formatterModule.addDeserializer(classOf[DateTime], CustomDateMapper.deserializer)
      formatterModule
    }

    def fromJson(date: String): DateTime = try {
      fmt.parseDateTime(date)
    } catch {
      case e: Exception => Logger.error(s"unable to parse date $date", e)
      null
    }

    def toJson(date: DateTime): String = fmt.print(date)

    private val fmt = DateTimeFormat.forPattern(Format)

    private val serializer = new JsonSerializer[DateTime] {
      override def serialize(date: DateTime, jgen: JsonGenerator, provider: SerializerProvider) = {
        jgen.writeString(toJson(date))
      }
    }

    private val deserializer = new JsonDeserializer[DateTime] {
      override def deserialize(jp: JsonParser, ctxt: DeserializationContext) = fromJson(jp.getText)
    }
  }

}