package models

import testutils.PlayFunSpec

class EmployeeSpec extends PlayFunSpec {
  describe("dome"){
    it("should"){
//      Employee.createOrUpdate(Employee("awesome"))
      val s: String = CouchDBConnection.client.serializer.toJson[Employee](Employee("awesome"))
      println(s)
      //{"decoratedName":null,"_id":null,"docType":"Employee","name":"awesome"}
      //We wish decoratedName not to appear
    }
  }
}
