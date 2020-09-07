package pt.iselearning.services.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ServerError
import pt.iselearning.services.service.AuthenticationService
import pt.iselearning.services.util.CHALLENGE_PATTERN
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
            "/v0/challenges/questionnaires/**" to AuthenticationFilter::shouldNotFilterChallengeRequest,
            "/v0/challenges/random" to AuthenticationFilter::shouldNotFilterChallengeRequest,
            QUESTIONNAIRE_ANSWER_PATTERN to AuthenticationFilter::shouldNotFilterQuestionnaireAnswerRequest,
            "/v0/questionnaires/**" to AuthenticationFilter::shouldNotFilterQuestionnairePattern
    )

    private val optionalAuthenticationPaths : HashMap<String, HashSet<HttpMethod>> = hashMapOf(
            CHALLENGE_PATTERN to hashSetOf<HttpMethod>(HttpMethod.GET),
            "${CHALLENGE_PATTERN}/**" to hashSetOf<HttpMethod>(HttpMethod.GET),
            "${CHALLENGE_PATTERN}/**/tags" to hashSetOf<HttpMethod>(HttpMethod.GET)
    )

    private fun optionalAuthentication(request: HttpServletRequest) : User? {
        val pathMatcher = AntPathMatcher()
        val authenticationHeader = request.getHeader("Authorization")
        for (key in optionalAuthenticationPaths.keys) {
            if(pathMatcher.match(key, request.servletPath) &&
                    optionalAuthenticationPaths[key]!!.map { e -> e.name }.contains(request.method)) {
                return if(authenticationHeader == null) null
                else authenticationService.validateAuthenticationHeader(authenticationHeader);
            }
        }
        return authenticationService.validateAuthenticationHeader(authenticationHeader)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val user = optionalAuthentication(request)
            request.setAttribute("loggedUser", user)
            filterChain.doFilter(request, response)
            return
        }catch (ex : ServiceException){
            response.status = ex.errorCode.httpCode
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/problem+json" )
            response.characterEncoding = "UTF-8"
            val error = ServerError(
                    request.servletPath,
                    ex.message,
                    ex.detail,
                    ex.instance
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

    private fun shouldNotFilterQuestionnaireAnswerRequest(request: HttpServletRequest) : Boolean =
            request.method != HttpMethod.GET.name ||
                    !AntPathMatcher().match(QUESTIONNAIRE_ANSWER_PATTERN, request.servletPath)

    private fun shouldNotFilterQuestionnairePattern(request: HttpServletRequest) : Boolean =
            request.method == HttpMethod.GET.name

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val pathMatcher = AntPathMatcher()
        for (key in validateFilters.keys){
            if(pathMatcher.match(key, request.servletPath)){
                val method = validateFilters[key] ?: return false
                return method(this, request)
            }
        }
        return false
    }

}


