package pt.iselearning.services.domain.executable

import javax.validation.constraints.NotNull

/**
* Data class that represents the object containing code, and unit tests to be execute
*/
data class Executable(

        @NotNull
        var code : String,

        @NotNull
        var unitTests : String,

        @NotNull
        var executeTests : Boolean
)