package org.gotson.nestor.infrastructure.email

import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.infrastructure.email.console.ConsoleEmailSender
import org.gotson.nestor.infrastructure.email.simple.SimpleEmailSender
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [EmailSender::class])
class EmailAutoWiringTestConfiguration

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
@ActiveProfiles("default")
class AutowiringConsoleEmailSenderTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `given default profile when autowiring then ConsoleEmailSender is loaded`() {
    assertThat(ctx.getBean(EmailSender::class.java))
        .isInstanceOf(ConsoleEmailSender::class.java)
  }
}

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
@ActiveProfiles("email")
class AutowiringSimpleEmailSenderTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `given email profile when autowiring then SimpleEmailSender is loaded`() {
    assertThat(ctx.getBean(EmailSender::class.java))
        .isInstanceOf(SimpleEmailSender::class.java)
  }
}