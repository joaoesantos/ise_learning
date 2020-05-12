package pt.iselearning.services.service

import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import java.util.*

class UserServices {
    fun checkIfUserExists(user: Optional<User>, userId: Int) {
        if (user.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This user does not exist.", // dar fix quando houver refactoring
                    "There is no user with id: $userId")
        }
    }
}