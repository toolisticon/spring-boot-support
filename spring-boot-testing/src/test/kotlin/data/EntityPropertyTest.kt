package io.toolisticon.spring.testing.data

import io.toolisticon.spring.testing.SpringBootTestingFixtures.Person
import io.toolisticon.spring.testing.SpringBootTestingFixtures.PersonProperty
import io.toolisticon.spring.testing.SpringBootTestingSupport.longIdSequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EntityPropertyTest {

  @Test
  fun `setId on person`() {
    val ids = longIdSequence(10)
    val person = Person(name = "Kermit")
    assertThat(person.id).isNull()

    val id = PersonProperty.prepareSave(person, ids)
    assertThat(id).isEqualTo(10)
    assertThat(person.id).isEqualTo(10)
  }
}
