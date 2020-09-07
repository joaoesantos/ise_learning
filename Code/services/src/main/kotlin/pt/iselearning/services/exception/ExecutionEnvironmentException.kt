package pt.iselearning.services.exception

import org.springframework.http.HttpStatus

class ExecutionEnvironmentException(
        val type: String,
        val title: String,
        val detail: String,
        val instance: String,
        val status: HttpStatus
): Exception()