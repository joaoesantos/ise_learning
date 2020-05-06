package pt.iselearning.services.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:executionEnvironments.properties")
data class RemoteExecutionUrls (
    @Value( "\${execution.environment.java}" )
    var javaExecutionEnvironment : String,

    @Value( "\${execution.environment.kotlin}" )
    var kotlinExecutionEnvironment : String,

    @Value( "\${execution.environment.javascript}" )
    var javascriptExecutionEnvironment : String,

    @Value( "\${execution.environment.c#}" )
    var cSharpExecutionEnvironment : String,

    @Value( "\${execution.environment.python}" )
    var pythonExecutionEnvironment : String
)