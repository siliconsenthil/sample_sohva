package models.traits

import utils.StringUtils
import scala.collection.mutable
import com.fasterxml.jackson.annotation.{JsonIgnore, JsonProperty}


case class ValidationErrors() extends mutable.HashMap[String, mutable.Set[String]] with mutable.MultiMap[String, String] {
  def add(field: String, message: String) = addBinding(field, message)

  def on(field: String) = getOrElse(field, Set())
}

trait Validation {
  private val toValidate: mutable.Buffer[Validation] = mutable.ListBuffer()

  private lazy val validateAndPopulateErrors = validate()

  private lazy val validationErrors = ValidationErrors()

  @JsonIgnore
  def isValid: Boolean = {
    validateAndPopulateErrors
    toValidate.map{v => v.isValid}.forall(el => el) && errors.isEmpty
  }

  @JsonProperty
  def errors = validationErrors

  protected def validate(): Unit = {
    // Override and populate errors
  }

  def blankValidation(attrName: String, attrValue: String) = {
    addErrorIf(StringUtils.isBlank(attrValue), attrName, "cannot be blank")
  }

  def blankValidation(attrName: String, attrValue: Seq[AnyRef], message: String = "cannot be blank") = {
    addErrorIf(attrValue == null || attrValue.isEmpty, attrName, message)
  }

  private[models] def uniquenessValidation(attrName: String, attrValue: String, existingValues: List[String]) = {
    addErrorIf(existingValues.exists(_.equalsIgnoreCase(attrValue)), attrName, "Already taken")
  }


  protected def addErrorIf(isNotValid: Boolean, attrName: String, message: String) = {
    if (isNotValid) validationErrors.add(attrName, message)
    !isNotValid
  }

  protected def childrenToValidate(groups: Seq[Validation]) = if (groups != null) toValidate ++= groups
}