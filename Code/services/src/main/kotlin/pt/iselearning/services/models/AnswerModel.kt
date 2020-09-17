package pt.iselearning.services.models

import javax.validation.constraints.Size

class AnswerModel(

        @field:Size(min = 0, max = 20, message = "Programing language description must less than 20 characters")
        var codeLanguage : String?,

        var answerCode : String?,

        var unitTests : String?

)