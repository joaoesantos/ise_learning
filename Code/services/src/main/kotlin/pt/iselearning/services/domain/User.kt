package pt.iselearning.services.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

/**
 *
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
    val name : String?,

    @Column("password")
    val password : String?
)
