package pt.iselearning.services.domain

import org.springframework.validation.annotation.Validated

/**
* data class that represents the result of an execution
*/
@Validated
data class Executable(
        var language : String?,
        var code : String?,
        var unitTests : String?,
        var executeTests : Boolean?
) {
}