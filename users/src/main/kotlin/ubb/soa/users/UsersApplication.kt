package ubb.soa.users

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@SpringBootApplication
class UsersApplication

fun main(args: Array<String>) {
    runApplication<UsersApplication>(*args)
}

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    val username: String,
    val password: String
)

interface UserRepository: JpaRepository<User, UUID> {
    fun findByUsernameAndPassword(username: String, password: String): User?
}

@RestController
class UserController(
    val userRepository: UserRepository
) {
    @PutMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
    ): UUID? {
        return userRepository.findByUsernameAndPassword(username, password)?.id
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): String {
        return userRepository.getById(id).username
    }

    @GetMapping
    fun findAll(): List<User> {
        return userRepository.findAll().map { it.copy(password = "") }
    }
}