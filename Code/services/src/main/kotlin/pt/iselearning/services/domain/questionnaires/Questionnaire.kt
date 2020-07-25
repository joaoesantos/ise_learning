package pt.iselearning.services.domain.questionnaires

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.Constants
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

/**
 * Data class that represents questionnaire entity
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

        @field:Positive(message = "Timer value must be positive")
        @Column(name = "timer", insertable = false, updatable = true)
        var timer: Int?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Generated(GenerationTime.INSERT)
        @Column(name = "creation_date", insertable = false, updatable = false)
        var creationDate: Date?,

        @field:Positive(message = "Creator id must be positive")
        @Column(name = "creator_id")
        var creatorId: Int?,

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinTable(name = "questionnaire_challenge")
        var questionnaireChallenge: QuestionnaireChallenge?

) { constructor() : this(null, null, null, null, null, null) }