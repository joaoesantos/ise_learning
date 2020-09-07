package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.iselearning.services.exception.error.ErrorCode
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import pt.iselearning.services.exception.ExecutionEnvironmentException
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ServerError
import pt.iselearning.services.exception.error.ValidationError
import java.lang.StringBuilder
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

/**
 * Handles all api exceptions
 * @return ResponseEntity in application/problem+json format
 */
@ControllerAdvice
class ServerExceptionHandler(
        private val responseHeaders: HttpHeaders = HttpHeaders()
) : ResponseEntityExceptionHandler() {

    init {
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    }

    /**
     * Handles exception of type ServerException
     */
    @ExceptionHandler(value = [ServiceException::class])
    fun handleServerException(
            request: HttpServletRequest,
            ex: ServiceException
    ) : ResponseEntity<Any> {
        return createResponseEntity(
                ServerError(
                        request.requestURL.toString(),
                        ex.title,
                        ex.detail,
                        ex.instance
                ),
                headers = responseHeaders,
                status = ErrorCode.convertToHttpStatus(ex.errorCode.httpCode)
        )
    }

    /**
     * Handles exception of type ConstraintViolationException
     */
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleValidationException(
            request: HttpServletRequest,
            ex: ConstraintViolationException
    ): ResponseEntity<Any> {

        val data = mutableListOf<Any>()
        if(ex.constraintViolations.isNotEmpty()) {
            ex.constraintViolations.iterator().forEach {
                data.add(
                        ValidationError(
                                it.messageTemplate,
                                it.invalidValue.toString(),
                                it.propertyPath.toString()
                        ))
            }
        }
        return createResponseEntity(
                ServerError(
                        request.requestURL.toString(),
                        ErrorCode.VALIDATION_ERROR.toString(),
                        StringBuilder().append("Error in function ")
                                .append(ex.message?.split(".")?.first()).toString(),
                        "/iselearning/constraintViolations",
                        data
                ),
                headers = responseHeaders,
                status = HttpStatus.BAD_REQUEST
        )
    }

    /**
     * Handles exception of type ExecutionEnvironmentException
     */
    @ExceptionHandler(value = [ExecutionEnvironmentException::class])
    fun handleExecutionEnvironmentException(
            request: HttpServletRequest,
            ex: ExecutionEnvironmentException
    ): ResponseEntity<Any> {
        return createResponseEntity(
                ServerError(
                        ex.type,
                        ex.title,
                        ex.detail,
                        ex.instance
                ),
                headers = responseHeaders,
                status = ex.status
        )
    }

    /**
     * Handles unexpected classes of exceptions
     */
    @ExceptionHandler(value = [Exception::class])
    fun handleException(
            request: HttpServletRequest,
            ex: Exception
    ): ResponseEntity<Any> {
        return createResponseEntity(
                ServerError(
                        request.requestURL.toString(),
                        ex.message!!,
                        ex.toString(),
                        "/unexpectedException"
                ),
                headers = responseHeaders,
                status = HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    /**
     * Creates response entity
     * @param serverError detailed information on occurred error in application/problem+json format
     * @param headers http response header
     * @param status http response status code
     */
    private fun createResponseEntity(serverError : ServerError, headers: HttpHeaders, status: HttpStatus) : ResponseEntity<Any>{
        return ResponseEntity(serverError, headers, status)
    }
}