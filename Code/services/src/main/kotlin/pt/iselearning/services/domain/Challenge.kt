package pt.iselearning.services.domain

import org.springframework.validation.annotation.Validated
import javax.persistence.*
import javax.validation.constraints.Positive

/**
 * data class that represents challenge entity
 */
@Validated
@Entity
@Table(name="challenge")
data class Challenge (
    @Id
    @Column(name="challenge_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @field:Positive(message = "Challenge id must be positive")
    var challengeId : Int? =  null,

    @Column(name="creator_id")
    @field:Positive(message = "Creator id must be positive")
    var creatorId : Int?,

    @Column(name="challenge_text")
    var challengeText : String?,

    @Column(name="is_private")
    var isPrivate : Boolean?,

    @OneToMany(cascade = [CascadeType.ALL], targetEntity=Solution::class)
    @JoinColumn(name="challenge_id", nullable=false, updatable=true, insertable=true)
    var solutions : MutableList<Solution>?
) {
    constructor() : this(null, null, null, null, null)
}