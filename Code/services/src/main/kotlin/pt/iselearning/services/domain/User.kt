package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

/**
 * data class that represents user entity
 */

@Table("user_app")
data class User (
    @Id
    @Column("id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id : Int? =  null,

    @Column("username")
    val username : String?,

    @Column("email")
    val email : String?,

    @Column("name")
    //@JsonProperty("name")
    val name : String?,

    @Column("password")
    val password : String?
)
