package org.gotson.nestor.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [PersistenceService::class])
class PersistenceAutoWiringTestConfiguration

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.dynamodb.region=ap-southeast-1"])
@ContextConfiguration(classes = [PersistenceAutoWiringTestConfiguration::class])
class AutowiringTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `PersistenceService is loaded when required property is defined`() {
    assertThat(ctx.getBean(PersistenceService::class.java))
        .isInstanceOf(PersistenceService::class.java)
  }
}

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [PersistenceAutoWiringTestConfiguration::class])
class AutowiringTest2 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `PersistenceService is not loaded when required property is missing`() {
    assertThatThrownBy { ctx.getBean(PersistenceService::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}