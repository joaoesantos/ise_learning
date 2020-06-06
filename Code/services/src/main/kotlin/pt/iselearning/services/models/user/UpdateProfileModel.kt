package pt.iselearning.services.models.user

import org.springframework.web.multipart.MultipartFile

class UpdateProfileModel(
        //TODO remove after authentication is done
        val userId : Int,
        val name : String?,
        val email : String?
)