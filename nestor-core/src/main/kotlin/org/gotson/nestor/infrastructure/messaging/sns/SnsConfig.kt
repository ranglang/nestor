package org.gotson.nestor.infrastructure.messaging.sns

import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(name = ["amazon.sns.region"])
@Configuration
class SnsConfig {

  @Bean
  fun amazonSnsClient(
      @Value("\${amazon.sns.region}") region: String
  ): AmazonSNSClient =
      AmazonSNSClientBuilder.standard()
          .withRegion(region)
          .build() as AmazonSNSClient
}