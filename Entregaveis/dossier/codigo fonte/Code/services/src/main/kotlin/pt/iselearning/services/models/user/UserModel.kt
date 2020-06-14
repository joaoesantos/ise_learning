package pt.iselearning.services.models.user

class UserModel (
        val userId : Int? =  null,

        val username : String?,

        var email : String?,

        var name : String?
) {
    constructor() : this(null, null, null, null)
}