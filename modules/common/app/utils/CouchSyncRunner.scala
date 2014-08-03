package utils

import gnieh.sohva.{CouchException, ConflictException, async}
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CouchSyncRunner {

  implicit class CouchSyncRunner[T](asyncResult: async.AsyncResult[T]) {
    def sync: T = {
      Await.result(asyncResult, Duration.Inf) match {
        case Right(t) => t
        case Left((409, error)) =>
          throw new ConflictException(error)
        case Left((code, error)) =>
          throw new CouchException(code, error)
      }
    }
  }

}
