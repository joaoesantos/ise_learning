package pt.iselearning.services.service

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.mindrot.jbcrypt.BCrypt
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.iselearning.services.domain.User
import pt.iselearning.services.exception.IselearningException
import pt.iselearning.services.exception.error.ErrorCode
import pt.iselearning.services.repository.UserRepository
import pt.iselearning.services.models.user.CreateUserModel
import pt.iselearning.services.models.user.UpdateProfileModel
import pt.iselearning.services.models.user.UserModel
import java.lang.Exception
import java.util.*

@Service
class UserService(
        private val modelMapper: ModelMapper,
        private val userRepository: UserRepository,
        private val emailValidator: EmailValidator
        ) {

    fun checkIfUserExists(user : Optional<User>, userId: Int) {
        if (user.isEmpty) {
            throw IselearningException(ErrorCode.ITEM_NOT_FOUND.httpCode, "This user does not exist.", // dar fix quando houver refactoring
                    "There is no user with id: $userId")
        }
    }

    @Transactional
    fun createUser(createUserModel : CreateUserModel) : UserModel {
        val encryptedPassword = BCrypt.hashpw(createUserModel.password, BCrypt.gensalt())
        createUserModel.password = encryptedPassword
        val user = modelMapper.map(createUserModel, User::class.java)
        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    @Transactional
    fun deleteUser(userId : Int)  {
        val user = verifyUser(userId = userId)
        userRepository.deleteByUsername(user.username!!)
        val exists = userRepository.existsByUsername(user.username)
        if(exists) {
            throw IselearningException(ErrorCode.UNEXPECTED_ERROR.httpCode, "Unable to delete user with id:$userId", "Unable to delete user")
        }

    }

    fun getUserInformation(username : String) : User = verifyUser(username)

    fun getAllUsers() : Iterable<UserModel> = userRepository.findAll().map { user -> modelMapper.map(user, UserModel::class.java )  }

    fun getUserById(userId: Int) : UserModel = modelMapper.map(verifyUser(userId = userId), UserModel::class.java)

    @Transactional
    fun updatePassword(password : String, userId : Int): UserModel {
        val user = verifyUser(userId = userId)
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        user.password = encryptedPassword

        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    @Transactional
    fun updateUserInformation(updateProfileModel: UpdateProfileModel, userId: Int) : UserModel {
        if(!emailValidator.isValid(updateProfileModel.email, null)){
            throw IselearningException(
                    ErrorCode.VALIDATION_ERROR.httpCode,
                    "Invalid Email inserted",
                    "Invalid Email"
            )
        }
        val user = verifyUser(userId = userId)
        user.email = updateProfileModel.email
        user.name = updateProfileModel.name

        return modelMapper.map(userRepository.save(user), UserModel::class.java)
    }

    private fun verifyUser(username : String? = null, userId : Int? = null) : User {

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