package models

import models.traits.CouchDBConnectionBase
import play.api.Play
import play.api.Play.current
import utils.CouchSyncRunner._

object CouchDBConnection extends CouchDBConnectionBase{
  override val username = "sample"
  override val password = "sample"
  override val dbName = "sample_sohva"

  override def refreshViews() = {
  }
}
