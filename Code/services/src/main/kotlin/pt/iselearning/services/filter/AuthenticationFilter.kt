package pt.iselearning.services.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.ServiceError
import pt.iselearning.services.service.AuthenticationService
import pt.iselearning.services.util.QUESTIONNAIRE_ANSWER_PATTERN
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KFunction2

/**
 * Class responsible to validate user credentials
 */
class AuthenticationFilter(private val authenticationService: AuthenticationService, private val objectMapper: ObjectMapper) : OncePerRequestFilter() {
    private val validateFilters : HashMap<String, KFunction2<AuthenticationFilter, HttpServletRequest, Boolean>> = hashMapOf(
            "/v0/challenges/**" to AuthenticationFilter::shouldNotFilterChallengeRequest,
            QUESTIONNAIRE_ANSWER_PATTERN to AuthenticationFilter::shouldNotFilterQuestionnaireAnswerRequest
    )

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authenticationHeader = request.getHeader("Authorization")
        try {
            val user = authenticationService.validateAuthenticationHeader(authenticationHeader);
            request.setAttribute("loggedUser", user)
            filterChain.doFilter(request, response)
            return
        }catch (ex : IselearningException){
            response.status = ex.errorCode
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/problem+json" )
            response.characterEncoding = "UTF-8"
            val error = ServiceError(
                    request.servletPath,
                    ex.message,
                    ex.outMessage,
                    request.requestURI
            )
            val json = objectMapper.writeValueAsString(error)
            response.outputStream.flush();
            response.outputStream.println(json);
            return
        }

    }

    private fun shouldNotFilterChallengeRequest(request: HttpServletRequest) : Boolean {
        return request.method == HttpMethod.GET.name
    }

    private fun shouldNotFilterQuestionnaireAnswerRequest(request: HttpServletRequest) : Boolean {
        return request.method != HttpMethod.GET.name
                ||
                !AntPathMatcher().match(QUESTIONNAIRE_ANSWER_PATTERN, request.servletPath)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val method = validateFilters[request.servletPath] ?: return false

        return method(this, request)
    }

}