package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.domain.Executable
import pt.iselearning.services.domain.ExecutableResult
import pt.iselearning.services.service.ExecutionService

/**
 * Handler responsible to respond to requests regarding code execution
 */
@RestController
@RequestMapping("/v0/execute")
class ExecutionController (private val executionService: ExecutionService) {
    /**
     * Method to create an user.
     * A json object that represents a object of the type User must be present in the body
     * @param Executable represents code to be executed
     * @return ResponseEntity<ExecutableResult> represents the result
     */
    @PostMapping
    fun createChallenge(@RequestBody executable: Executable): ResponseEntity<ExecutableResult> {
        return ResponseEntity.ok().body(executionService.execute(executable))
    }
}


