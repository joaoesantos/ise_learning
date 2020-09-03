package pt.iselearning.services.models.user

import javax.validation.constraints.Size

class UserPasswordModel (

        @field:Size(min = 0, max = 255, message = "Password must have less than 255 characters")
        var password : String

)