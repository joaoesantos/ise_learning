package pt.iselearning.services.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
@PropertySource("classpath:application.properties")

data class DatasourceProperties (
        @Value( "\${iselearning.services.host}" )
        val host : String,
        @Value( "\${iselearning.services.port}" )
        val port : Int,
        @Value( "\${iselearning.services.database}" )
        val database : String,
        @Value( "\${iselearning.services.username}" )
        val username : String,
        @Value( "\${iselearning.services.password}" )
        val password : String,
        @Value( "\${iselearning.services.driverClassName}" )
        val driverClassName : String,
        @Value( "\${iselearning.services.providerUrl}" )
        val providerUrl : String
)