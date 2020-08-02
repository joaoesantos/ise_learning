package pt.iselearning.services.domain.executable

/**
* data class that represents the result of an execution
*/
data class ExecutableWithNoLanguage(
        var code : String?,
        var unitTests : String?,
        var executeTests : Boolean?
) {
}