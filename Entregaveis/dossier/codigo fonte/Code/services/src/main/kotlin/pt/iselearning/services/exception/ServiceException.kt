package pt.iselearning.services.exception

import pt.iselearning.services.exception.error.ErrorCode
import java.lang.RuntimeException

class ServiceException(
        val title: String,
        val detail : String,
        val instance : String,
        val errorCode : ErrorCode
): RuntimeException()