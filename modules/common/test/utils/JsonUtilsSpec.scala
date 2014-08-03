package utils

import org.joda.time.DateTime
import org.scalatest.Matchers._
import org.scalatest.FunSpec

class JsonUtilsSpec extends FunSpec {
  describe("custom date mapper") {
    it("should serialize date to dd/mm/yyyy format") {
      val date = new DateTime().withDate(2014, 7, 2)
      val json = JsonUtils.toJson(Map("date" -> date))
      json should be("""{"date":"02/07/2014"}""")
    }

    it("should de-serialize date from dd/mm/yyy format"){
      val deSerializedDate = JsonUtils.fromJson[Map[String, DateTime]]("""{"date":"02/08/2014"}""")
      deSerializedDate.getOrElse("date", null) should be(new DateTime().withDate(2014, 8, 2).toDateMidnight)
    }

    it("should return null if not able to deserialize"){
      val deSerializedDate = JsonUtils.fromJson[Map[String, DateTime]]("""{"date":"invalid"}""")
      deSerializedDate.getOrElse("date", null) should be(null)
    }
  }
}
