package org.gotson.nestor.infrastructure.messaging.sns

import org.gotson.nestor.domain.service.Destination
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "amazon.sns")
class SnsConfigurationProperties {
    var enabled: Boolean? = null
    var region: String? = null
    var topics: List<SnsTopic> = ArrayList()
}

class SnsTopic {
    var arn: String? = null
    var destination: Destination? = null
}