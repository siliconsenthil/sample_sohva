package utils

object Tapper {
  implicit class Tapper[A](obj: A) {
    def tap(block: A => Unit): A = {
      block(obj)
      obj
    }
  }
}