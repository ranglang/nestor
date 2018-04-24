package org.gotson.nestor.infrastructure.persistence.repository

import org.gotson.nestor.infrastructure.persistence.dto.WishedClassDynamo
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository
import java.time.DayOfWeek

@EnableScan
interface WishedClassRepository : CrudRepository<WishedClassDynamo, String> {
    fun findByDay(day: DayOfWeek): List<WishedClassDynamo>
}