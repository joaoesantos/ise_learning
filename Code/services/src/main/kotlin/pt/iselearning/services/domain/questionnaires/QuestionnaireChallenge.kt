package pt.iselearning.services.domain.questionnaires

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.Challenge
import pt.iselearning.services.util.Constants
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents questionnaire-challenge entity
 */
@Validated
@Entity
@Table(name = "qc", schema = Constants.SCHEMA)
class QuestionnaireChallenge (

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Positive(message = "Questionnaire-Challenge id must be positive")
    var qcId: Int?,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "questionnaire_id", nullable = false, updatable = false, insertable = true)
    var questionnaire: Questionnaire?,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false, insertable = true)
    var challenge : Challenge?,

    @Column(name="language_filter")
    var languageFilter : String?

) { constructor() : this(null, null, null, null) }