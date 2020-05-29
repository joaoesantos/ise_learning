package pt.iselearning.services.transfer

import org.springframework.web.multipart.MultipartFile

class UpdateProfileModel(
        val name : String?,
        val image : MultipartFile?,
        val mail : String?
)