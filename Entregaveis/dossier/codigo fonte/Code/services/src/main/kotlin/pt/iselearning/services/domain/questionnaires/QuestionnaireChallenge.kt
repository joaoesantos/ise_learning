package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.challenge.Challenge
import pt.iselearning.services.util.SCHEMA
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * Data class that represents questionnaire-challenge entity
 */
@Validated
@Entity
@Table(name = "qc", schema = SCHEMA)
class QuestionnaireChallenge (

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @field:Positive(message = "Questionnaire-Challenge id must be positive")
        var qcId: Int?,

        @ManyToOne
        @JoinColumn(name = "questionnaire_id", nullable = false, updatable = false, insertable = true)
        var questionnaire: Questionnaire?,

        @ManyToOne
        @JoinColumn(name = "challenge_id", nullable = false, updatable = false, insertable = true)
        var challenge: Challenge?,

        @Column(name="language_filter")
        var languageFilter: String?

) { constructor() : this(null, null, null, null) }