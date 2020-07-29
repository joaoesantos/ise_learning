package pt.iselearning.services.domain

import pt.iselearning.services.util.Constants
import javax.persistence.Entity
import javax.persistence.*

/**
 * data class that represents user entity
 */
@Entity
@Table(name="user_app", schema = Constants.SCHEMA)
data class User (
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val userId : Int? =  null,

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
