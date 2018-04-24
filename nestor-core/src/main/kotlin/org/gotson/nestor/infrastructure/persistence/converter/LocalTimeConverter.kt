package org.gotson.nestor.infrastructure.persistence.converter

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import java.time.LocalTime

class LocalTimeConverter : DynamoDBTypeConverter<String, LocalTime> {
    override fun unconvert(stringValue: String?): LocalTime = LocalTime.parse(stringValue)
    override fun convert(time: LocalTime?): String = time.toString()
}