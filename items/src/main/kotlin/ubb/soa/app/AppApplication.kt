package ubb.soa.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@SpringBootApplication
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}

@Entity
@Table(name = "items")
class Item(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    val description: String,
    val userId: UUID
)

interface ItemRepository: JpaRepository<Item, UUID> {
    fun findAllByUserId(userId: UUID): List<Item>
}

@RestController
@RequestMapping("/items")
class ItemController(
    val itemRepository: ItemRepository
) {
    @GetMapping
    fun getAll(): List<Item> {
        return itemRepository.findAll()
    }

    @GetMapping("/{userId}")
    fun getAllForUserId(@PathVariable userId: UUID): List<Item> {
        return itemRepository.findAllByUserId(userId)
    }
}