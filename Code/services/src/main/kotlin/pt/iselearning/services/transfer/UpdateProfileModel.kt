package pt.iselearning.services.transfer

import org.springframework.web.multipart.MultipartFile

class UpdateProfileModel(
        //TODO remove after authentication is done
        val userId : Int,
        val name : String?,
        val email : String?
)