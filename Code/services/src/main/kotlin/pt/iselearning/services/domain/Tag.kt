package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents the tag entity
 */
@Entity
@Table(name="tag")
data class Tag (
    @Id
    @Column(name="tag_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var tagId : Int? =  null,

    @Column(name="tag")
    var tag : String?

    /*
    @OneToMany
    @JoinColumn(name="tag", nullable=false, updatable=true, insertable=true)
    var challengeTag: List<ChallengeTag>?*/

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "tags")
    @JoinTable(
        name = "ct",
        joinColumns = [JoinColumn(name = "tag")],
        inverseJoinColumns =[JoinColumn(name = "challenge_id")]
    )*/
    /*@OneToMany(mappedBy = "challenge")
    var challenges : List<ChallengeTag>?*/
) {
    constructor() : this(null, null)
}
