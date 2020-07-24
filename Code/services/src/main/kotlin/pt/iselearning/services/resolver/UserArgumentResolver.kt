package pt.iselearning.services.resolver

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pt.iselearning.services.domain.User
import javax.servlet.http.HttpServletRequest

/**
 * Class responsible to obtain an instance of User when one is necessary in the controllers
 */
class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer?,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory?): Any?
            = webRequest.getNativeRequest(HttpServletRequest::class.java)!!.getAttribute("loggedUser")
}