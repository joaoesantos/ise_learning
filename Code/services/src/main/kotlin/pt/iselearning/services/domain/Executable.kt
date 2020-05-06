package pt.iselearning.services.domain

/**
* data class that represents the result of an execution
*/
data class Executable(
        var language : String?,
        var code : String?,
        var unitTests : String?,
        var executeTests : Boolean?
) {
}