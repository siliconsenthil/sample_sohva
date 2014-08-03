package testutils

import org.scalatest.FunSpec
import play.api.test.Helpers._
import play.api.test.FakeApplication
import models.{CouchDBConnection, CouchDBRepository}
import utils.CouchSyncRunner._


class PlayFunSpec extends FunSpec {
  override def withFixture(test: NoArgTest) = {
    val application = FakeApplication()
    running(application) {
     CouchDBConnection.database.delete.sync
     CouchDBConnection.database.create.sync
     CouchDBConnection.refreshViews()

      super.withFixture(test)
    }
  }
}
