package org.gotson.nestor.infrastructure.email.ses

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(name = ["amazon.ses.region"])
@Configuration
class SesConfig {

  @Bean
  fun amazonSesClient(
      @Value("\${amazon.ses.region}") region: String
  ): AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard()
      .withRegion(region)
      .build()
}