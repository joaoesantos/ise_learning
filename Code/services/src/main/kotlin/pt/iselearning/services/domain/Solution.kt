package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.*

/**
 * data class that represents solution entity
 */

@Table("challenge_solution")
data class Solution (
    @Id
    @Column("challengeSolutionId")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id : Int? =  null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name="challengeId", referencedColumnName = "challengeId")
    var challengeId : Int? =  null,

    @Column("challengeCode")
    var challengeCode : String?,

    @Column("codeLanguage")
    var username : String?,

    @Column("solutionCode")
    var email : String?,

    @Column("unitTests")
    var name : String?
)
