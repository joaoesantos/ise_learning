package pt.iselearning.services.transfer

import org.springframework.web.multipart.MultipartFile

class UpdateProfileModel(
        val userId : Int,
        val name : String?,
        val image : MultipartFile?,
        val email : String?
)