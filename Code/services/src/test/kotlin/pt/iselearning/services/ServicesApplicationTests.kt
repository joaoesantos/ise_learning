package pt.iselearning.services

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.iselearning.services.domain.User
import pt.iselearning.services.repository.UserRepository

@SpringBootTest
class ServicesApplicationTests {

	@Autowired
	private lateinit var userRepository : UserRepository

	@Test
	fun insertUser() {
		val count = userRepository.findAll().count().block()
		val expected = count!! + 1
		val user = User(null,"xpto2020", "xpto@email.com", "xpto","12345")
		val newUser = userRepository.save(user).block()
		val actual = userRepository.findAll().count().block()
		Assert.assertNotNull(newUser)
		Assert.assertNotNull(newUser!!.id)
		Assert.assertEquals(expected, actual)
	}

}
