package pt.iselearning.services.domain

import org.springframework.validation.annotation.Validated
import pt.iselearning.services.util.Constants
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents challenge answer entity
 */
@Validated
@Entity
@Table(name="challenge_answer", schema = Constants.SCHEMA)
data class ChallengeAnswer (
    @Id
    @Column(name="challenge_answer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @field:Positive(message = "Challenge Answer id must be positive")
    var challengeAnswerId : Int? =  null,

    @Column(name="challenge_id")
    @field:Positive(message = "Challenge id must be positive")
    var challengeId : Int?,

    @Column(name="user_id")
    @field:Positive(message = "User id must be positive")
    var userId : Int?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="answer_id", nullable=false, updatable=true, insertable=true)
    var answer : Answer?
) {
    constructor() : this(null, null, null, null)
}
