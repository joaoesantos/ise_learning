package pt.iselearning.services.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:executionEnvironments.properties")
data class RemoteExecutionUrls (
    @Value( "\${execution.environment.java}" )
    val javaExecutionEnvironment : String,

    @Value( "\${execution.environment.kotlin}" )
    val kotlinExecutionEnvironment : String,

    @Value( "\${execution.environment.javascript}" )
    val javascriptExecutionEnvironment : String,

    @Value( "\${execution.environment.c#}" )
    val cSharpExecutionEnvironment : String,

    @Value( "\${execution.environment.python}" )
    val pythonExecutionEnvironment : String
)