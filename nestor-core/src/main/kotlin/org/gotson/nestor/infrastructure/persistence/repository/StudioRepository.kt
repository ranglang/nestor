package org.gotson.nestor.infrastructure.persistence.repository

import org.gotson.nestor.infrastructure.persistence.dto.StudioDynamo
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface StudioRepository : CrudRepository<StudioDynamo, String>