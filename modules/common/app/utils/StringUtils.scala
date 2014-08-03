package utils

object StringUtils {
  def isBlank(string: String) = Option(string).getOrElse("").isEmpty
}
