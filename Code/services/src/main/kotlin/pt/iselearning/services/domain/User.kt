package pt.iselearning.services.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
    @JsonProperty("id")
    val id : Int? =  null,

    @Column("username")
    @JsonProperty("username")
    val username : String?,

    @Column("email")
    @JsonProperty("email")
    val email : String?,

    @Column("name")
    @JsonProperty("name")
    val name : String?,

    @Column("password")
    @JsonProperty("password")
    val password : String?
)
