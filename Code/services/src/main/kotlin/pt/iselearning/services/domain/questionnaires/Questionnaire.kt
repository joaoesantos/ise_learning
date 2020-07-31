package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents questionnaire entity
 */
@Validated
@Entity
@Table(name="questionnaire")
data class Questionnaire (

        @Id
        @Column(name="questionnaire_id")
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire id must be positive")
        var questionnaireId: Int,

        @Column(name="description")
        var description: String? = null,

        @Column(name="timer")
        @field:Positive(message = "timer must be positive")
        var timer: Int,

        @Column(name="creationDate")
        var creationDate: Date,

        @Column(name="creator_id")
        @field:Positive(message = "Creator id must be positive")
        var creatorId: Int

)