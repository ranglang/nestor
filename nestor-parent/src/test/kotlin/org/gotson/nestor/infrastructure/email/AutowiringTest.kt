package org.gotson.nestor.infrastructure.email

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gotson.nestor.infrastructure.email.console.ConsoleEmailSender
import org.gotson.nestor.infrastructure.email.ses.SesEmailSender
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [EmailSender::class])
class EmailAutoWiringTestConfiguration

@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
@ActiveProfiles("noemail")
class AutowiringTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `ConsoleEmailSender is loaded when profile noemail is active`() {
    assertThat(ctx.getBean(EmailSender::class.java))
        .isInstanceOf(ConsoleEmailSender::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.ses.region=ap-southeast-1", "amazon.ses.from=test@test.com"])
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
class AutowiringTest2 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SesEmailSender is loaded when required properties are defined`() {
    assertThat(ctx.getBean(EmailSender::class.java))
        .isInstanceOf(SesEmailSender::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.ses.region=ap-southeast-1", "amazon.ses.from=test@test.com"])
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
@ActiveProfiles("noemail")
class AutowiringTest3 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SesEmailSender is not loaded when required properties are defined but noemail profile is active`() {
    assertThat(ctx.getBean(EmailSender::class.java))
        .isNotInstanceOf(SesEmailSender::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
class AutowiringTest4 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SesEmailSender is not loaded when no profile is defined but required properties are missing`() {
    assertThatThrownBy { ctx.getBean(EmailSender::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.ses.region=ap-southeast-1"])
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
class AutowiringTest5 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SesEmailSender is not loaded when no profile is defined but propery amazon-ses-from is missing`() {
    assertThatThrownBy { ctx.getBean(EmailSender::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.ses.from=test@test.com"])
@ContextConfiguration(classes = [EmailAutoWiringTestConfiguration::class])
class AutowiringTest6 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SesEmailSender is not loaded when no profile is defined but but propery amazon-ses-region is missing`() {
    assertThatThrownBy { ctx.getBean(EmailSender::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}