package pt.iselearning.services.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pt.iselearning.services.configuration.RemoteExecutionUrls
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.domain.executable.ExecutableResult
import pt.iselearning.services.domain.executable.Executable
import pt.iselearning.services.exception.ExecutionEnvironmentException
import pt.iselearning.services.exception.error.ServerError
import javax.validation.Valid

/**
 * This class contains the business logic associated with the actions on execution environments
 */
@Validated
@Service
class ExecutionEnvironmentsService(
        private val remoteExecUrls: RemoteExecutionUrls
) {

    private val languageUrlMap: Map<String, String>

    init {
        languageUrlMap = HashMap()
        languageUrlMap.put("java", remoteExecUrls.javaExecutionEnvironment)
        languageUrlMap.put("kotlin", remoteExecUrls.kotlinExecutionEnvironment)
        languageUrlMap.put("javascript", remoteExecUrls.javascriptExecutionEnvironment)
        languageUrlMap.put("csharp", remoteExecUrls.cSharpExecutionEnvironment)
        languageUrlMap.put("python", remoteExecUrls.pythonExecutionEnvironment)
    }

    /**
     * Method to call execution environment that executes code with or without unit tests
     * @param executableModel code to be execute for a specific programing language
     * @return an executable result
     */
    @Validated
    fun execute(@Valid executableModel: ExecutableModel): ExecutableResult {
        var errorCode: HttpStatus? = null
        val executionEnvironmentUrl = languageUrlMap[executableModel.language]
        val response = WebClient.create(executionEnvironmentUrl!!)
                .post()
                .body(
                        BodyInserters.fromValue(
                                Executable(executableModel.code, executableModel.unitTests, executableModel.executeTests)
                        )
                )
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