package org.gotson.nestor.infrastructure.selenium

import mu.KotlinLogging
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class PureDriverBuilder @Autowired constructor(
    @Value("\${chrome.driver.wait}") private val chromeDriverWait: Long,
    @Value("\${nestor.pure.format.time}") private val timeFormat: String,
    @Value("\${nestor.pure.format.date}") private val dateFormat: String
) {

  fun build(): PureDriver {
    return PureDriver(
        chromeOptionsForEnvironment(),
        chromeDriverWait,
        timeFormat,
        dateFormat)
  }

  private fun chromeOptionsForEnvironment(): ChromeOptions {
    val options = ChromeOptions()

    val defaultArguments = listOf("--headless")
    options.addArguments(defaultArguments)
    logger.debug { "Default arguments: $defaultArguments" }

    return options
  }
}