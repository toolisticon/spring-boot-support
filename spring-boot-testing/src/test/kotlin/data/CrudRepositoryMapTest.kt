package io.toolisticon.spring.testing.data

import io.toolisticon.spring.testing.SpringBootTestingFixtures
import io.toolisticon.spring.testing.SpringBootTestingFixtures.Person
import io.toolisticon.spring.testing.SpringBootTestingFixtures.PersonCrudRepository
import io.toolisticon.spring.testing.SpringBootTestingSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Optional


class CrudRepositoryMapTest {

  class PersonCrudRepositoryMap : CrudRepositoryMap<Person, Long>(
    entityProperty = SpringBootTestingFixtures.PersonProperty,
    idGenerator = SpringBootTestingSupport.longIdSequence()
  ), PersonCrudRepository {
    override fun findByName(name: String): Optional<Person> = Optional.ofNullable(store.values.first { name == it.name })
  }

  private val repo = PersonCrudRepositoryMap()

  @Test
  fun `initial empty`() {
    assertThat(repo.count()).isEqualTo(0)
  }

  @Test
  fun `save and find`() {
    val person = Person(name = "Kermit")
    assertThat(person.id).isNull()
    val saved = repo.save(person)
    assertThat(saved.id).isEqualTo(0)

    assertThat(repo.findById(0).map(Person::name)).hasValue(person.name)
  }


}
