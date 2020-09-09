package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.domain.executable.ExecutableModel
import pt.iselearning.services.domain.executable.ExecutableResult
import pt.iselearning.services.service.ExecutionEnvironmentsService
import pt.iselearning.services.util.VERSION

/**
 * Handler responsible to respond to requests regarding code execution
 */
@RestController
@RequestMapping("/${VERSION}/execute")
class ExecutionController (private val executionEnvironmentsService: ExecutionEnvironmentsService) {
    /**
     * Method to execute code.
     *
     * @param executableModel represents code to be executed
     * @return ResponseEntity<ExecutableResult> represents the result
     */
    @PostMapping(name = "execute")
    fun execute(@RequestBody executableModel: ExecutableModel): ResponseEntity<ExecutableResult> {
        return ResponseEntity.ok().body(executionEnvironmentsService.execute(executableModel))
    }
}


