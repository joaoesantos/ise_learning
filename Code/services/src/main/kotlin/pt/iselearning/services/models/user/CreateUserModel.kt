package pt.iselearning.services.models.user

class CreateUserModel (
        val username : String,
        val name : String,
        val email : String,
        var password : String
        )