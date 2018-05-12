package org.gotson.nestor.infrastructure.messaging

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gotson.nestor.infrastructure.messaging.console.ConsolePublisher
import org.gotson.nestor.infrastructure.messaging.sns.SnsMessagePublisher
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
@ComponentScan(basePackageClasses = [MessagePublisher::class])
class MessagingAutoWiringTestConfiguration

@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(classes = [MessagingAutoWiringTestConfiguration::class])
@ActiveProfiles("nosns")
class AutowiringTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `ConsolePublisher is loaded when nosns profile is active`() {
    assertThat(ctx.getBean(MessagePublisher::class.java))
        .isInstanceOf(ConsolePublisher::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.sns.enabled=true", "amazon.sns.region=ap-southeast-1"])
@ContextConfiguration(classes = [MessagingAutoWiringTestConfiguration::class])
class AutowiringTest2 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SnsMessagePublisher is loaded when required properties are defined`() {
    assertThat(ctx.getBean(MessagePublisher::class.java))
        .isInstanceOf(SnsMessagePublisher::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.sns.enabled=true", "amazon.sns.region=ap-southeast-1"])
@ContextConfiguration(classes = [MessagingAutoWiringTestConfiguration::class])
@ActiveProfiles("nosns")
class AutowiringTest3 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SnsMessagePublisher is not loaded when required properties are defined but nosns profile is active`() {
    assertThat(ctx.getBean(MessagePublisher::class.java))
        .isNotInstanceOf(SnsMessagePublisher::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.sns.region=ap-southeast-1"])
@ContextConfiguration(classes = [MessagingAutoWiringTestConfiguration::class])
class AutowiringTest4 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SnsMessagePublisher is not loaded when no profile is defined but property amazon-sns-enabled is missing`() {
    assertThatThrownBy { ctx.getBean(MessagePublisher::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.sns.enabled=true"])
@ContextConfiguration(classes = [MessagingAutoWiringTestConfiguration::class])
class AutowiringTest5 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `SnsMessagePublisher is not loaded when no profile is defined but property amazon-sns-region is missing`() {
    assertThatThrownBy { ctx.getBean(MessagePublisher::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}