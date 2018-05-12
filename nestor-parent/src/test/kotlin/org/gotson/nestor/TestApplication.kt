package org.gotson.nestor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TestApplication {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      SpringApplication.run(TestApplication::class.java, *args)
    }
  }
}