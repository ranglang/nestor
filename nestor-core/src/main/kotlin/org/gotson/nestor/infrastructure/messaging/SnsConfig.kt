package org.gotson.nestor.infrastructure.messaging

import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SnsConfig {

    @Bean
    fun amazonSnsClient(
            @Value("\${amazon.region}") region: String
    ): AmazonSNSClient = AmazonSNSClientBuilder.standard()
            .withRegion(region)
            .build() as AmazonSNSClient
}