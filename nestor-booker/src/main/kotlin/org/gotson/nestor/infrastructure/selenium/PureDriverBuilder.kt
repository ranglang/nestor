package org.gotson.nestor.infrastructure.selenium

import mu.KotlinLogging
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Paths

private val logger = KotlinLogging.logger {}

@Service
class PureDriverBuilder @Autowired constructor(
        @Value("\${chrome.driver.wait}") private val chromeDriverWait: Long,
        @Value("\${chrome.driver.path}") private val chromeDriverPath: String,
        @Value("\${chrome.headless.path}") private val chromeHeadlessPath: String,
        @Value("\${nestor.booker.pure.format.time}") private val timeFormat: String,
        @Value("\${nestor.booker.pure.format.date}") private val dateFormat: String
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

        val lambdaTaskroot = System.getenv("LAMBDA_TASK_ROOT")

        if (lambdaTaskroot != null) {
            logger.debug { "AWS Lambda detected, setting specific environment" }
            logger.debug { "LAMBDA_TASK_ROOT: $lambdaTaskroot" }

            val driverPath = Paths.get(lambdaTaskroot, chromeDriverPath).toAbsolutePath().toString()
            System.setProperty("webdriver.chrome.driver", driverPath)

            logger.debug { "ChromeDriver absolute path: $driverPath" }
            logger.debug { "System webdriver.chrome.driver: ${System.getProperty("webdriver.chrome.driver")}" }

            val chromePath = Paths.get(lambdaTaskroot, chromeHeadlessPath).toAbsolutePath().toString()
            options.setBinary(chromePath)
            logger.debug { "Chrome absolute path: $chromePath" }

            val defaultArguments = listOf(
                    "--headless",
                    "--disable-gpu",
                    "--window-size=1280x1696",
                    "--no-sandbox",
                    "--user-data-dir=/tmp/user-data",
                    "--hide-scrollbars",
                    "--enable-logging",
                    "--log-level=0",
                    "--v=99",
                    "--single-process",
                    "--data-path=/tmp/data-path",
                    "--ignore-certificate-errors",
                    "--homedir=/tmp",
                    "--disk-cache-dir=/tmp/cache-dir"
            )
            options.addArguments(defaultArguments)
            logger.debug { "Default arguments: $defaultArguments" }
        }
        return options
    }
}