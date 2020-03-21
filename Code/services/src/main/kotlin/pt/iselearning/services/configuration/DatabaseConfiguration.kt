package pt.iselearning.services.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.nio.file.Paths

/**
 * Singleton object responsible to store information necessary to start database connection
 */
object DatabaseConfiguration {
    var props : Properties = Properties()

    /**
     *
     */
    data class Properties (
            val host : String = "",
            val port : Int = 0,
            val database : String = "",
            val username : String = "",
            val password : String = ""
    )

    /**
     * Initiation of the singleton object
     */
    init {
        val path = Paths.get("src","main","resources", "databaseAccess.json")
        val mapper = jacksonObjectMapper()
        val json = mapper.readValue(path.toFile(), Properties::class.java)
        this.props = Properties(json.host, json.port, json.database, json.username, json.password)
    }

}