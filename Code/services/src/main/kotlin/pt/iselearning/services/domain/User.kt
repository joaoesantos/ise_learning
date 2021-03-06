package pt.iselearning.services.domain

import pt.iselearning.services.util.SCHEMA
import javax.persistence.Entity
import javax.persistence.*

/**
 * data class that represents user entity
 */
@Entity
@Table(name="app_user", schema = SCHEMA)
data class User (
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val userId : Int? =  null,

    @Column(name="username")
    val username : String?,

    @Column(name="email")
    var email : String?,

    @Column(name="name")
    var name : String?,

    @Column(name="password")
    var password : String?
) {
    constructor() : this(null, null, null, null, null)
}
