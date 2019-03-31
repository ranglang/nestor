package org.gotson.nestor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableSwagger2
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}