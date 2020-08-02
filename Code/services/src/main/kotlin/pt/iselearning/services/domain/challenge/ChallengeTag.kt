package pt.iselearning.services.domain.challenge

import pt.iselearning.services.domain.Tag
import pt.iselearning.services.util.Constants
import javax.persistence.*

/**
 * data class that represents the tag entity
 */
@Entity
@Table(name="ct", schema = Constants.SCHEMA)
data class ChallengeTag (
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var challengeTagId : Int? =  null,

    @Column(name="challenge_id")
    var challengeId : Int? =  null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="tag_id", nullable=false, updatable=false, insertable=true)
    var tag : Tag?
) {
    constructor() : this(null, null, null)
}


