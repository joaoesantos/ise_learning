package pt.iselearning.services.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pt.iselearning.services.configuration.RemoteExecutionUrls
import pt.iselearning.services.domain.executable.Executable
import pt.iselearning.services.domain.executable.ExecutableResult
import pt.iselearning.services.domain.executable.ExecutableWithNoLanguage
import pt.iselearning.services.exception.ExecutionEnvironmentException
import pt.iselearning.services.exception.error.ServerError
import javax.validation.Valid

/**
 * This class contains the business logic associated with the actions on execution environments
 */
@Validated
@Service
class ExecutionService {
    private val remoteExecUrls : RemoteExecutionUrls
    private val languageUrlMap : Map<String, String>

    constructor(remoteExecUrls: RemoteExecutionUrls) {
        this.remoteExecUrls = remoteExecUrls
        this.languageUrlMap = HashMap()
        this.languageUrlMap.put("java", remoteExecUrls.javaExecutionEnvironment)
        this.languageUrlMap.put("kotlin", remoteExecUrls.kotlinExecutionEnvironment)
        this.languageUrlMap.put("javascript", remoteExecUrls.javascriptExecutionEnvironment)
        this.languageUrlMap.put("csharp", remoteExecUrls.cSharpExecutionEnvironment)
        this.languageUrlMap.put("python", remoteExecUrls.pythonExecutionEnvironment)
    }

    @Validated
    fun execute(@Valid executable: Executable): ExecutableResult? {
        var errorCode: HttpStatus? = null
        val executionEnvironmentUrl = languageUrlMap[executable.language]
        val response = WebClient.create(executionEnvironmentUrl!!)
                .post()
                .body(BodyInserters.fromValue(
                        ExecutableWithNoLanguage(executable.code, executable.unitTests, executable.executeTests)))
                .exchange()
                .flatMap { clientResponse ->
                    if (clientResponse.statusCode().is2xxSuccessful) {
                        clientResponse.body { clientHttpResponse, _ -> clientHttpResponse.body }
                        return@flatMap clientResponse.bodyToMono(ExecutableResult::class.java)
                    } else {
                        errorCode = clientResponse.statusCode()
                        return@flatMap  clientResponse.bodyToMono(ServerError::class.java)
                    }
                }
                .block()
        if(errorCode != null) {
            val serverError = response as ServerError
            throw ExecutionEnvironmentException(
                    serverError.type,
                    serverError.title,
                    serverError.detail,
                    serverError.instance!!,
                    errorCode!!
            )
        } else {
            return response as ExecutableResult
        }
    }

}