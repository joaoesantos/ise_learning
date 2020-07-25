package pt.iselearning.services.exception.error

import org.springframework.http.HttpStatus
import java.lang.Exception

enum class ErrorCode(val httpCode : Int) {
    VALIDATION_ERROR(400),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    ITEM_NOT_FOUND(404),
    MANDATORY_VALUE(400),
    CONFLICT(409),
    UNEXPECTED_ERROR(500);

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