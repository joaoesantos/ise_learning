package pt.iselearning.services.exception.error

/**
 * Class that represents a validation error. Provides a message and the information to identify the invalid values
 */
class ValidationError (
        val message: String,
        val invalidValue: String,
        val propertyPath: String
)