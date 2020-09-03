package pt.iselearning.services.models.user

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class UserProfileModel(

        @field:Size(min = 0, max = 50, message = "Name must have less than 50 characters")
        val name : String,

        @field:Size(min = 0, max = 50, message = "Email must have less than 50 characters")
        val email : String
)