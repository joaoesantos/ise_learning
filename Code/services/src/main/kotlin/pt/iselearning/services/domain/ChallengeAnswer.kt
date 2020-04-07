package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.*

/**
 * data class that represents challenge answer entity
 */

@Table("challenge_answer")
data class ChallengeAnswer (
    @Id
    @Column("challengeAnswerId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id : Int? =  null,

    @Column("challengeId")
    var challengeId : Int?,

    @Column("userId")
    var userId : Int?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "answerId", referencedColumnName = "answerId")
    var answer : Answer?
)
