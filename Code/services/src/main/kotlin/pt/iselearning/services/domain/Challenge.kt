package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents challenge entity
 */
@Entity
@Table(name="challenge")
data class Challenge (
    @Id
    @Column(name="challenge_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var challengeId : Int? =  null,

    @Column(name="creator_id")
    var creatorId : Int?,

    @Column(name="challenge_text")
    var challengeText : String?,

    @Column(name="is_private")
    var isPrivate : Boolean?,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name="challenge_id", nullable=false, updatable=true, insertable=true)
    var solutions : List<Solution>?

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
            name = "ct",
            joinColumns = [JoinColumn(name = "challenge_id")],
            inverseJoinColumns =[JoinColumn(name = "tag")]
    )*/
    //@OneToMany(mappedBy = "tag")
    //var tags : List<ChallengeTag>?
) {
    constructor() : this(null, null, null, null, null)
}
