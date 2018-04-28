package org.gotson.nestor.infrastructure

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.gotson.nestor.domain.model.WishedClassDated
import org.junit.Test
import org.springframework.core.io.ClassPathResource

class MapperConfigTest {

    private val mapper = MapperConfig().getMapper()

    @Test
    fun `schedule event is deserialized without errors`() {
        val file = ClassPathResource("sample.scheduledEvent.json").file

        val scheduledEvent = mapper.readValue<ScheduledEvent>(file)

        assertThat(scheduledEvent).isNotNull()
    }

    @Test
    fun `sns event is deserialized without errors`() {
        val file = ClassPathResource("sample.snsEvent.json").file

        val snsEvent = mapper.readValue<SNSEvent>(file)

        assertThat(snsEvent).isNotNull()
    }

    @Test
    fun `sns message content is deserialized without errors`() {
        val file = ClassPathResource("sample.snsEvent.json").file

        val snsEvent = mapper.readValue<SNSEvent>(file)
        val wishedClassDated = mapper.readValue<WishedClassDated>(snsEvent.records[0].sns.message)

        assertThat(wishedClassDated).isNotNull()
    }
}