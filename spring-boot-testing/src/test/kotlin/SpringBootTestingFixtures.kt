package io.toolisticon.spring.testing

import io.toolisticon.spring.testing.data.EntityIdProperty
import jakarta.persistence.*
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

object SpringBootTestingFixtures {

  object PersonProperty : EntityIdProperty<Person, Long> {
    override fun getId(entity: Person): Optional<Long> = Optional.ofNullable(entity.id)
    override fun setId(entity: Person, id: Long): Person = entity.apply { this.id = id }
  }

  @Entity
  class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,
  )

  @Entity
  class PersonVersioned(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Version
    val version: Long? = null,

    @Column(nullable = false)
    val name: String,
  )

  @Repository
  interface PersonCrudRepository : CrudRepository<Person, Long> {
    fun findByName(name: String): Optional<Person>
  }

  @Repository
  interface PersonVersionedCrudRepository : CrudRepository<PersonVersioned, Long>
}
