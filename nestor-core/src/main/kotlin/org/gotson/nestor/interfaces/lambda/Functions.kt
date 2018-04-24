package org.gotson.nestor.interfaces.lambda

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.gotson.nestor.infrastructure.messaging.MessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

@Configuration
class Functions @Autowired constructor(
//        private val persistenceService: PersistenceService,
        private val pureBooker: org.gotson.nestor.domain.service.PureBooker,
        private val messagePublisher: MessagePublisher,
        private val mapper: ObjectMapper
) {

    @Bean
    fun dailyCron(): Function<ScheduledEvent, String> =
            Function {
                val requests = pureBooker.findMatchingWishedClasses()
                requests.forEach { messagePublisher.send(it) }
                "Sent ${requests.size} event(s)"
            }

//    @Bean
//    fun allClasses(): Function<ScheduledEvent, String> =
//            Function {
//                val allClasses = persistenceService.findAllWishedClass()
//                allClasses.map { it.dated(LocalDate.now()) }
//                        .forEach { messagePublisher.send(it) }
//                "Sent ${allClasses.size} event(s)"
//            }
//
//
//    @Bean
//    fun classesByDay(): Function<String, String> =
//            Function {
//                persistenceService.findWishedClassByDay(DayOfWeek.valueOf(it)).joinToString("\n")
//            }
//
//    @Bean
//    fun classesToBook(): Function<String, String> =
//            Function {
//                pureBooker.findMatchingWishedClasses().joinToString("\n")
//            }
//
//
//    @Bean
//    fun createUser(): Function<String, String> =
//            Function {
//                val user = mapper.readValue<CreateUserDto>(it).toDomain()
//                persistenceService.save(user).toString()
//            }
//
//    @Bean
//    fun createStudio(): Function<String, String> =
//            Function {
//                val studio = mapper.readValue<CreateStudioDto>(it).toDomain()
//                persistenceService.save(studio).toString()
//            }
//
//    @Bean
//    fun createMembership(): Function<String, String> =
//            Function {
//                val membership = mapper.readValue<CreateMembershipDto>(it).toDomain()
//                persistenceService.save(membership).toString()
//            }
//
//
//    @Bean
//    fun createWishedClass(): Function<String, String> =
//            Function {
//                val wishedClass = mapper.readValue<CreateWishedClassDto>(it).toDomain()
//                persistenceService.save(wishedClass).toString()
//            }
//
//
//    private fun CreateMembershipDto.toDomain(): Membership =
//            Membership(
//                    user = persistenceService.findOneUser(userId),
//                    studio = persistenceService.findOneStudio(studioId),
//                    login = login,
//                    password = password
//            )
//
//    private fun CreateWishedClassDto.toDomain(): WishedClass =
//            WishedClass(
//                    membership = persistenceService.findOneMembership(membershipId),
//                    time = time,
//                    day = day,
//                    location = location,
//                    type = type
//            )
}


