package pt.iselearning.services.transfer

class UserModel (
        val id : Int? =  null,

        val username : String?,

        var email : String?,

        var name : String?
) {
    constructor() : this(null, null, null, null)
}