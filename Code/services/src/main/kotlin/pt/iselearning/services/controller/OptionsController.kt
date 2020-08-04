package pt.iselearning.services.controller

import net.minidev.json.JSONObject
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pt.iselearning.services.util.Constants.Companion.VERSION

@RestController
@RequestMapping("/${VERSION}")
class OptionsController(
        private val handlerMapping: org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
) {

    @RequestMapping(method = [RequestMethod.OPTIONS], name = "getHandlerMappingMethods")
    fun getHandlerMappingMethods(): ResponseEntity<Any> {
        val handlerMappingMethods = mutableMapOf<String,JSONObject>()
        for (key in handlerMapping.handlerMethods.keys) {
            val jsonObject = JSONObject()
            jsonObject["method"] = "${key.methodsCondition.methods}".substringAfter("[").substringBefore("]")
            jsonObject["pattern"] = "${key.patternsCondition.patterns}".substringAfter("[").substringBefore("]")
            handlerMappingMethods[key.name.toString()] = jsonObject
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(handlerMappingMethods)
    }

}