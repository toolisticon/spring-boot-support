package io.toolisticon.spring.testing.data

import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class CrudRepositoryMap<T : Any, ID : Any>(
  val entityProperty: EntityIdProperty<T, ID>,
  val idGenerator: IdGenerator<ID>,
) : CrudRepository<T, ID> {

  protected val store: ConcurrentHashMap<ID, T> = ConcurrentHashMap()
  protected operator fun get(id: ID): T? = store[id]

  override fun <S : T> save(entity: S): S {
    val id = entityProperty.prepareSave(entity, idGenerator)
    store[id] = entity

    return entity
  }

  override fun <S : T> saveAll(entities: Iterable<S>): Iterable<S> = entities.map(::save)

  override fun findById(id: ID): Optional<T> = Optional.ofNullable(get(id))

  override fun existsById(id: ID): Boolean = store.containsKey(id)

  override fun findAll(): Iterable<T> = store.values

  override fun findAllById(ids: Iterable<ID>): Iterable<T> = if (store.keys.containsAll(ids.toSet())) {
    ids.mapNotNull(::get)
  } else {
    listOf()
  }

  override fun count() = store.size.toLong()

  override fun deleteById(id: ID) {
    store.remove(id)
  }

  override fun delete(entity: T) = entityProperty.getId(entity).ifPresent(::deleteById)

  override fun deleteAllById(ids: Iterable<ID>) = ids.forEach(::deleteById)

  override fun deleteAll(entities: Iterable<T>) = entities.forEach(::delete)

  override fun deleteAll() = store.clear()

  override fun toString(): String = "${this.javaClass.simpleName}(store=$store)"
}
