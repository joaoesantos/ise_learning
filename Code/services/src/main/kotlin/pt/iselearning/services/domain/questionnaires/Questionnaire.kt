package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * data class that represents questionnaire entity
 */
@Validated
@Entity
@Table(name="questionnaire", schema = "ise_learning")
data class Questionnaire (

        @Id
        @Column(name="questionnaire_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int,

        @field:Size(min = 1, max = 100, message = "Questionnaire description must be between 1 and 100 characters")
        @Column(name="description")
        var description: String? = null,

        @field:Positive(message = "timer must be positive")
        @Column(name="timer")
        var timer: Int,

        @Column(name="creationDate")
        var creationDate: Date,

        @field:Positive(message = "Creator id must be positive")
        @Column(name="creator_id")
        var creatorId: Int

)