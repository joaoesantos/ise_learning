package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.Constants
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

/**
 * data class that represents questionnaire entity
 */
@Validated
@Entity
@Table(name = "questionnaire", schema = Constants.SCHEMA)
data class Questionnaire (

        @Id
        @Column(name = "questionnaire_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int?,

        @field:Size(min = 1, max = 50, message = "Questionnaire description must be between 1 and 50 characters")
        @Column(name = "description")
        var description: String?,

        @field:PositiveOrZero(message = "Timer must be positive or zero")
        @Column(name = "timer")
        var timer: Int?,

        @Column(name = "creation_date")
        var creationDate: Date?,

        @field:Positive(message = "Creator id must be positive")
        @Column(name = "creator_id")
        var creatorId: Int?

) {
        constructor() : this(null, null, null, null, null)
}