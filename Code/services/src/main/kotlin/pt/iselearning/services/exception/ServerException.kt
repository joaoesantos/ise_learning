package pt.iselearning.services.exception

import pt.iselearning.services.exception.error.ErrorCode
import java.lang.RuntimeException

class ServerException(
        override val message : String,
        val debugMessage : String,
        val errorCode : ErrorCode
) : RuntimeException(message) {
}