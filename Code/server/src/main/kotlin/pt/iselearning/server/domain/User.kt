package pt.iselearning.server.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int = 1,

    @get: NotBlank
    val username : String = "username",

    @get: NotBlank
    val email: String = "email@example.com",

    @get: NotBlank
    val name: String = "Danilo Oliveira"
)