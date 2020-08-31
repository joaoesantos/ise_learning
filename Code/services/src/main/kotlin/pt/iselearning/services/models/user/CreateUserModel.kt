package pt.iselearning.services.models.user

import javax.validation.constraints.Size

class CreateUserModel (

        @field:Size(min = 0, max = 50, message = "User name must have less than 50 characters")
        var name : String,

        @field:Size(min = 0, max = 50, message = "User username must have less than 50 characters")
        var username : String,

        @field:Size(min = 0, max = 50, message = "User email must have less than 50 characters")
        var email : String,

        @field:Size(min = 0, max = 50, message = "User password must have less than 50 characters")
        var password : String

)