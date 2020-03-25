package pt.iselearning.services.exception

class IselearningException (
        val errorCode : Int = 0,
        val outMessage : String = "",
        message : String) : Exception(message)