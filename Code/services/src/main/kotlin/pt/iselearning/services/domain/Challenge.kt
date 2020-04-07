package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.OneToMany

/**
 * data class that represents challenge entity
 */

@Table("challenge")
data class Challenge (
    @Id
    @Column("challengeId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id : Int? =  null,

    @Column("creatorID")
    var creatorId : String?,

    @Column("challengeText")
    var challengeText : String?,

    @Column("isPrivate")
    var isPrivate : Boolean?,

    @OneToMany(mappedBy = "challengeId")
    var solutions : List<Solution>?
)
