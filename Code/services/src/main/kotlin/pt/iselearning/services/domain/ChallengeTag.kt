package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents the tag entity
 */
@Entity
@Table(name="ct")
@IdClass(ChallengeTagId::class)
data class ChallengeTag (
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var challengeTagId : Int? =  null,

    @Id
    @Column(name="challenge_id")
    var challengeId : Int? =  null,

    @Column(name="tag")
    var tag : String? =  null
    /*@ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="challenge_id", nullable=false, updatable=false, insertable=false)
    var challenge : Challenge?,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="tag", nullable=false, updatable=true, insertable=true)
    var tag : Tag?*/
) {
    constructor() : this(null, null)
}


