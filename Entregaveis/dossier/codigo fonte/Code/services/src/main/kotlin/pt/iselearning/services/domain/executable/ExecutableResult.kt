package pt.iselearning.services.domain.executable

import javax.validation.constraints.NotNull

/**
 * Data class that represents the result of an execution
 */
data class ExecutableResult (

        @NotNull
        val rawResult: String,

        @NotNull
        val wasError: Boolean,

        @NotNull
        val executionTime: Long
)