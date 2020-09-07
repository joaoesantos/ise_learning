package pt.iselearning.services.exception

import pt.iselearning.services.exception.error.ErrorCode
import java.lang.RuntimeException

class ServiceException(
        override val message : String,
        val detail : String,
        val instance : String,
        val errorCode : ErrorCode
): RuntimeException(message)