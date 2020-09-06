package pt.iselearning.services.service

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.mindrot.jbcrypt.BCrypt
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.ServerException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.models.user.CreateUserModel
import pt.iselearning.services.models.user.UserProfileModel
import pt.iselearning.services.models.user.UserModel
import java.lang.Exception
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Positive

/**
 * This class contains the business logic associated with the actions on the user object
 */
@Validated
@Service
class UserService(
        private val userRepository: UserRepository,
        private val emailValidator: EmailValidator,
        private val modelMapper: ModelMapper
) {

    /**
     * Create a user.
     *
     * @param createUserModel object information
     * @return created user
     */
    @Transactional
    fun createUser(@Valid createUserModel: CreateUserModel): UserModel {
        val encryptedPassword = BCrypt.hashpw(createUserModel.password, BCrypt.gensalt())
        createUserModel.password = encryptedPassword
        val user = modelMapper.map(createUserModel, User::class.java)
        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    /**
     * Get all users.
     *
     * @return List of user objects
     */
    fun getAllUsers(): Iterable<UserModel> = userRepository.findAll().map { user -> modelMapper.map(user, UserModel::class.java )  }

    /**
     * Get user by its unique identifier.
     *
     * @param userId identifier of questionnaire object
     * @return user object
     */
    fun getUserById(@Positive userId: Int): UserModel = modelMapper.map(verifyUser(userId = userId), UserModel::class.java)

    /**
     * Get user by its unique username.
     *
     * @param username unique username of user object
     * @return user object
     */
    fun getUserByUsername(username: String): User = verifyUser(username)

    /**
     * Update users information.
     *
     * @param userProfileModel information to be updated
     * @return updated user
     */
    @Transactional
    fun updateUserInformation(@Valid userProfileModel: UserProfileModel, @Positive userId: Int): UserModel {
        if(!emailValidator.isValid(userProfileModel.email, null)){
            throw IselearningException(
                    ErrorCode.VALIDATION_ERROR.httpCode,
                    "Invalid Email inserted",
                    "Invalid Email"
            )
        }
        val user = verifyUser(userId = userId)
        user.email = userProfileModel.email
        user.name = userProfileModel.name

        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    /**
     * Update users password.
     *
     * @param password information to be updated
     * @return updated user
     */
    @Transactional
    fun updatePassword(password: String, @Positive userId: Int): UserModel {
        val user = verifyUser(userId = userId)
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        user.password = encryptedPassword

        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    /**
     * Delete a user by its unique identifier.
     *
     * @param userId identifier of object
     */
    @Transactional
    fun deleteUser(@Positive userId: Int)  {
        val user = verifyUser(userId = userId)
        userRepository.deleteByUsername(user.username!!)
        val exists = userRepository.existsByUsername(user.username)
        if(exists) {
            throw IselearningException(ErrorCode.UNEXPECTED_ERROR.httpCode, "Unable to delete user with id:$userId", "Unable to delete user")
        }
    }

    /**
     * Validates if user exists, and return users in case of success.
     *
     * @param userId identifier of object
     * @throws ServerException when on failure to find questionnaire
     */
    private fun verifyUser(username: String? = null, @Positive userId: Int? = null): User {

        val optionalUser : Optional<User> = if(username == null && userId != null)
                                                userRepository.findById(userId)
                                            else if(username != null && userId == null)
                                                userRepository.findByUsername(username)
                                            else throw Exception("Invalid arguments")

        if(optionalUser.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode,"User with username:$username does not exist in our records", "User does not exist")
        }

        return optionalUser.get()
    }



}