package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.domain.executable.Executable
import pt.iselearning.services.domain.executable.ExecutableResult
import pt.iselearning.services.service.ExecutionService
import pt.iselearning.services.util.VERSION

/**
 * Handler responsible to respond to requests regarding code execution
 */
@RestController
@RequestMapping("/${VERSION}/execute")
class ExecutionController (private val executionService: ExecutionService) {
    /**
     * Method to execute code.
     *
     * @param executable represents code to be executed
     * @return ResponseEntity<ExecutableResult> represents the result
     */
    @PostMapping(name = "execute")
    fun execute(@RequestBody executable: Executable): ResponseEntity<ExecutableResult> {
        return ResponseEntity.ok().body(executionService.execute(executable))
    }
}


