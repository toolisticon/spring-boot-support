package io.toolisticon.spring.testing

import io.toolisticon.spring.testing.data.IdGenerator
import java.util.concurrent.atomic.AtomicLong

object SpringBootTestingSupport {

  @JvmOverloads
  fun longIdSequence(start: Long = 0) = object : IdGenerator<Long> {
    private val ids = AtomicLong(start)
    override fun next(): Long = ids.getAndIncrement()
  }

}
