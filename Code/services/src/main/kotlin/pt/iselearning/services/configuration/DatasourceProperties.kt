package pt.iselearning.services.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.nio.file.Paths

/**
 * Singleton object responsible to store information necessary to start database connection
 */
object DatasourceProperties {
        var props : Properties

        /**
         * Inner data class to hold information while reading json file that must be present in the resources folder
         */
        data class Properties (
                val host : String,
                val port : Int,
                val database : String,
                val username : String,
                val password : String,
                val driverClassName : String,
                val providerUrl : String
        )

        /**
         * Initiation of the singleton object
         */
        init {
            val path = Paths.get("src","main","resources", "databaseAccess.json")
            val mapper = jacksonObjectMapper()
            val json = mapper.readValue(path.toFile(), Properties::class.java)
            this.props = Properties(
                    json.host,
                    json.port,
                    json.database,
                    json.username,
                    json.password,
                    json.driverClassName,
                    json.providerUrl)
        }
}