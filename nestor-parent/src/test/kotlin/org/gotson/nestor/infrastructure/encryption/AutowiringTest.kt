package org.gotson.nestor.infrastructure.encryption

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.gotson.nestor.infrastructure.encryption.kms.AwsKmsEncryptionService
import org.gotson.nestor.infrastructure.encryption.plain.PlaincryptionService
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
@ComponentScan(basePackageClasses = [EncryptionService::class])
class EncryptionAutoWiringTestConfiguration


@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(classes = [EncryptionAutoWiringTestConfiguration::class])
@ActiveProfiles("plaincrypt")
class AutowiringPlainTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `PlaincryptionService is loaded when profile plaincrypt is active`() {
    assertThat(ctx.getBean(EncryptionService::class.java))
        .isInstanceOf(PlaincryptionService::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.kms.region=ap-southeast-1"])
@ContextConfiguration(classes = [EncryptionAutoWiringTestConfiguration::class])
class AutowiringAwsKmsTest {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `AwsKmsEncryptionService is loaded when required property is defined`() {
    assertThat(ctx.getBean(EncryptionService::class.java))
        .isInstanceOf(AwsKmsEncryptionService::class.java)
  }
}

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["amazon.kms.region=ap-southeast-1"])
@ContextConfiguration(classes = [EncryptionAutoWiringTestConfiguration::class])
@ActiveProfiles("plaincrypt")
class AutowiringAwsKmsTest2 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `AwsKmsEncryptionService is not loaded when required property is defined but plaincrypt profile is active`() {
    assertThat(ctx.getBean(EncryptionService::class.java))
        .isNotInstanceOf(AwsKmsEncryptionService::class.java)
  }
}

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [EncryptionAutoWiringTestConfiguration::class])
class AutowiringAwsKmsTest3 {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `AwsKmsEncryptionService is not loaded when no profile is defined but amazon-kms-region property is missing`() {
    assertThatThrownBy { ctx.getBean(EncryptionService::class.java) }
        .isInstanceOf(NoSuchBeanDefinitionException::class.java)
  }
}
