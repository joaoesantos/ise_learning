package pt.iselearning.services.domain

import javax.persistence.Entity
import javax.persistence.*

/**
 * data class that represents user entity
 */
@Entity
@Table(name="app_user")
data class User (
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id : Int? =  null,

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
