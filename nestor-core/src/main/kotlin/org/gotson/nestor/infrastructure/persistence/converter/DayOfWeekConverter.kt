package org.gotson.nestor.infrastructure.persistence.converter

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import java.time.DayOfWeek

class DayOfWeekConverter : DynamoDBTypeConverter<String, DayOfWeek> {
    override fun unconvert(stringValue: String?): DayOfWeek = DayOfWeek.valueOf(stringValue!!)
    override fun convert(dayOfWeek: DayOfWeek?): String = dayOfWeek.toString()
}