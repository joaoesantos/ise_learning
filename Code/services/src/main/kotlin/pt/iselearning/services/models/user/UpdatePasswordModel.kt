package pt.iselearning.services.models.user

import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class UpdatePasswordModel (

        @field:Positive(message = "User id must be positive")
        val userId: Int,

        @field:Size(min = 0, max = 255, message = "Password must have less than 255 characters")
        var password : String

)