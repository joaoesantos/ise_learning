package pt.iselearning.services.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pt.iselearning.services.configuration.RemoteExecutionUrls
import pt.iselearning.services.domain.Executable
import pt.iselearning.services.domain.ExecutableResult
import pt.iselearning.services.domain.ExecutableWithNoLanguage

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
        this.languageUrlMap.put("c#", remoteExecUrls.cSharpExecutionEnvironment)
        this.languageUrlMap.put("python", remoteExecUrls.pythonExecutionEnvironment)
    }

    fun execute(executable: Executable): ExecutableResult? {
        val executionEnvironmentUrl = languageUrlMap.get(executable.language)
        val response = WebClient.create(executionEnvironmentUrl!!).post().body(
                BodyInserters.fromValue(
                        ExecutableWithNoLanguage(executable.code, executable.unitTests, executable.executeTests)))
                .retrieve()
        return response.bodyToMono(ExecutableResult::class.java).block()
    }
}