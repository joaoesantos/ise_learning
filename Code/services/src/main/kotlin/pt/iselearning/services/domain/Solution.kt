package pt.iselearning.services.domain

import javax.persistence.*

/**
 * data class that represents solution entity
 */
@Entity
@Table(name="challenge_solution")
data class Solution (
    @Id
    @Column(name="challenge_solution_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var solutionId : Int? =  null,

    @Column(name="challenge_code")
    var challengeCode : String?,

    @Column(name="code_language")
    var codeLanguage : String?,

    @Column(name="solution_code")
    var solutionCode : String?,

    @Column(name="unit_tests")
    var unitTests : String?
) {
    constructor() : this(null, null, null, null, null)
}
