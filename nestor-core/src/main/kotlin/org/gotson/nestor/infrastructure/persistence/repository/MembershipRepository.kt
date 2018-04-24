package org.gotson.nestor.infrastructure.persistence.repository

import org.gotson.nestor.infrastructure.persistence.dto.MembershipDynamo
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface MembershipRepository : CrudRepository<MembershipDynamo, String>