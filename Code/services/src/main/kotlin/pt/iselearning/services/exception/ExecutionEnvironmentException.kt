package pt.iselearning.services.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class ExecutionEnvironmentException(
        val type: String,
        val title: String,
        val detail: String,
        val instance: String,
        val status: HttpStatus
): RuntimeException()