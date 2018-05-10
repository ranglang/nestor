package org.gotson.nestor.infrastructure.persistence

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.gotson.nestor.infrastructure.persistence.repository.WishedClassRepository
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@ConditionalOnProperty(name = ["amazon.dynamodb.region"])
@Configuration
@EnableDynamoDBRepositories(basePackageClasses = [WishedClassRepository::class])
class DynamoConfig(
    @Value("\${amazon.dynamodb.region}") private val region: String
) {

  @Bean(name = ["amazonDynamoDB"])
  fun getDynamoDB(): AmazonDynamoDB {
    return AmazonDynamoDBClientBuilder
        .standard()
        .withRegion(region)
        .build()
  }
}