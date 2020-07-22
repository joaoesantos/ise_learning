package pt.iselearning.services.exception.error

import org.springframework.http.HttpStatus
import java.lang.Exception

enum class ErrorCode(val httpCode : Int) {
    VALIDATION_ERROR(400),
    ITEM_NOT_FOUND(404),
    UNEXPECTED_ERROR(500),
    UNAUTHORIZED(401),
    MANDATORY_VALUE(400);

    companion object {
        fun convertToHttpStatus(errorCode : Int) : HttpStatus{
            for (status in HttpStatus.values()){
                if (status.value() == errorCode){
                    return status
                }
            }
            throw Exception()
        }
    }
}