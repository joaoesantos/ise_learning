package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents challenge answer entity
 */
@Entity
@Table(name="challenge_answer")
data class ChallengeAnswer (
    @Id
    @Column(name="challenge_answer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var challengeAnswerId : Int? =  null,

    @Column(name="challenge_id")
    var challengeId : Int?,

    @Column(name="user_id")
    var userId : Int?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="answer_id", nullable=false, updatable=true, insertable=true)
    var answer : Answer?
) {
    constructor() : this(null, null, null, null)
}
