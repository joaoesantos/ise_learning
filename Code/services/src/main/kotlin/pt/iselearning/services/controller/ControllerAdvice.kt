package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.ServiceError
import pt.iselearning.services.exception.error.ErrorCode
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ControllerAdvice(private val responseHeaders: HttpHeaders = HttpHeaders()) : ResponseEntityExceptionHandler() {
    init {
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    }

    @ExceptionHandler(value = [IselearningException::class])
    fun iseLearningExceptionHandler(servlet: HttpServletRequest, ex : IselearningException) : ResponseEntity<ServiceError> {
        return createResponseEntity(
                ServiceError(
                        servlet.servletPath,
                        ex.message,
                        ex.outMessage,
                        servlet.requestURI
                ),
                headers = responseHeaders,
                status = ErrorCode.convertToHttpStatus(ex.errorCode)
        )
    }

    private fun createResponseEntity(
            serviceError: ServiceError,
            headers: HttpHeaders,
            status: HttpStatus): ResponseEntity<ServiceError> {
        return ResponseEntity.status(status).headers(headers).body(serviceError)
    }
}