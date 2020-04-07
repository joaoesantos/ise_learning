package pt.iselearning.services.domain

import javax.persistence.Entity
import javax.persistence.*

/**
 * data class that represents user entity
 */
@Entity
@Table(name="user_app")
data class User (
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id : Int? =  null,

    @Column(name="username")
    val username : String?,

    @Column(name="email")
    val email : String?,

    @Column(name="name")
    //@JsonProperty("name")
    val name : String?,

    @Column(name="password")
    val password : String?
)
