package org.gotson.nestor.infrastructure.encryption.kms

import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.AWSKMSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(name = ["amazon.kms.region"])
@Configuration
class KmsConfig {

  @Bean
  fun amazonKmsClient(
      @Value("\${amazon.kms.region}") region: String
  ): AWSKMS = AWSKMSClientBuilder.standard()
      .withRegion(region)
      .build()
}