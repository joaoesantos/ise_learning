package pt.iselearning.services.service

import org.mindrot.jbcrypt.BCrypt
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.ServiceException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.models.user.UserModel
import pt.iselearning.services.repository.UserRepository
import java.util.*

/**
 * Class responsible to maintain and validate business logic regarding authentication
 */
@Service
class AuthenticationService (
        private val userRepository: UserRepository,
        private val modelMapper: ModelMapper
) {

    /**
     * Method responsible to get logged user
     *
     * @param authorizationHeader header present in an http request with AUTHENTICATION as key
     * @return userModel that matched the username and password inserted
     */
    fun getLoggedInUser(authorizationHeader: String) : UserModel {
        val user = validateAuthenticationHeader(authorizationHeader)
        return modelMapper.map(user, UserModel::class.java)
    }

    /**
     * Method responsible to validate credentials
     *
     * @param authorizationHeader header present in an http request with AUTHENTICATION as key
     * @return user that matched the username and password inserted
     */
    fun validateAuthenticationHeader(authorizationHeader: String?): User {
        if(authorizationHeader == null){
            throw ServiceException(
                    "Invalid credentials inserted",
                    "Authorization header nonexistent",
                    "/iselearning/authentication/noAuthorizationHeader",
                    ErrorCode.UNAUTHORIZED
            )
        }
        val authenticationHeaderSplit = authorizationHeader.split(" ")

        if(authenticationHeaderSplit.size != 2){
            throw ServiceException(
                    "Invalid authentication schema",
                    "Invalid schema format",
                    "/iselearning/authentication/invalidAuthenticationSchema",
                    ErrorCode.UNAUTHORIZED
            )
        }

        val authenticationSchema = authenticationHeaderSplit[0]
        if (authenticationSchema != "Basic"){
            throw ServiceException(
                    "Invalid authentication schema",
                    "Authentication Schema: $authenticationSchema is not supported",
                    "/iselearning/authentication/unsupportedAuthenticationSchema",
                    ErrorCode.UNAUTHORIZED
            )
        }
        val base64Credentials = authenticationHeaderSplit[1]
        val byteContent = Base64.getDecoder().decode(base64Credentials)
        val credentials = String(byteContent)

        val credentialsSplit = credentials.split(":")

        if(credentialsSplit.size != 2){
            throw ServiceException(
                    "Invalid authentication schema",
                    "Authentication Schema: $authenticationSchema is not supported",
                    "/iselearning/authentication/unsupportedAuthenticationSchema",
                    ErrorCode.UNAUTHORIZED
            )
        }

        val username = credentialsSplit[0]
        val password = credentialsSplit[1]

        val optionalUser = userRepository.findByUsername(username)

        if(!optionalUser.isPresent){
            throw ServiceException(
                    "Invalid credentials inserted",
                    "Username and/or password don't match our records",
                    "/iselearning/authentication/unmatchedUsernamePassword",
                    ErrorCode.UNAUTHORIZED
            )
        }

        val user = optionalUser.get()

        if(!BCrypt.checkpw(password, user.password)){
            throw ServiceException(
                    "Invalid credentials inserted",
                    "Username and/or password don't match our records",
                    "/iselearning/authentication/unmatchedUsernamePassword",
                    ErrorCode.UNAUTHORIZED
            )
        }

        return user
    }

}