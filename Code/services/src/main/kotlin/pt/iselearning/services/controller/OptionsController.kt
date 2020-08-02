package pt.iselearning.services.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v0/options")
class OptionsController(
        private val handlerMapping: org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
) {

    @GetMapping("/getHandlerMappingMethods")
    fun getHandlerMappingMethods(): ResponseEntity<Any> {
        val handlerMappingMethods = mutableListOf<String>()
        for (key in handlerMapping.handlerMethods.keys) {
            handlerMappingMethods.add("${key.methodsCondition.methods}${key.patternsCondition.patterns}")
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(handlerMappingMethods)
    }

}