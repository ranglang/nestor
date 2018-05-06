package org.gotson.nestor.infrastructure.email

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SesConfig {

    @Bean
    fun amazonSesClient(
            @Value("\${amazon.ses.region}") region: String
    ): AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard()
            .withRegion(region)
            .build()
}