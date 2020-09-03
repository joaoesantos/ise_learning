package pt.iselearning.services.models.user

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class UserModel (

        @field:Positive(message = "User id must be positive")
        val userId: Int?,

        @field:Size(min = 0, max = 255, message = "Name must have less than 255 characters")
        var name: String?,

        @field:Size(min = 0, max = 255, message = "Username must have less than 50 characters")
        val username: String?,

        @field:Size(min = 0, max = 255, message = "Email must have less than 50 characters")
        var email: String?

) {
    constructor() : this(null, null, null, null)
}