package pt.iselearning.services.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.iselearning.services.models.codeLanguage.CodeLanguageModel
import pt.iselearning.services.service.CodeLanguageService

/**
 * Handler responsible to respond to requests regard code languages entity
 */
@RestController
@RequestMapping("/v0/codeLanguages")
class CodeLanguageController (private val codeLanguageService: CodeLanguageService) {
    /**
     * Method to get all existing code languages.
     */
    @GetMapping
    fun getAllCodeLanguages(): ResponseEntity<List<CodeLanguageModel>> {
        return ResponseEntity.ok().body(codeLanguageService.getAllCodeLanguages())
    }
}


