package pt.iselearning.services.service

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pt.iselearning.services.configuration.RemoteExecutionUrls
import pt.iselearning.services.domain.executable.Executable
import pt.iselearning.services.domain.executable.ExecutableResult
import pt.iselearning.services.domain.executable.ExecutableWithNoLanguage
import javax.validation.Valid

@Validated
@Service
class ExecutionService {
    private val remoteExecUrls : RemoteExecutionUrls
    private val languageUrlMap : Map<String, String>

    constructor(remoteExecUrls : RemoteExecutionUrls){
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
        val executionEnvironmentUrl = languageUrlMap[executable.language]
        val response = WebClient.create(executionEnvironmentUrl!!).post().body(
                BodyInserters.fromValue(
                        ExecutableWithNoLanguage(executable.code, executable.unitTests, executable.executeTests)))
                .retrieve()
        return response.bodyToMono(ExecutableResult::class.java).block()
    }
}