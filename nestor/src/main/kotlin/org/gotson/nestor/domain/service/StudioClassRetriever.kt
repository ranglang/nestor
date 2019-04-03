package org.gotson.nestor.domain.service

import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.infrastructure.selenium.PureDriverBuilder
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class StudioClassRetriever(
    private val pureDriverBuilder: PureDriverBuilder
) {

  fun retrieveAll(studio: Studio, dates: Iterable<LocalDate>): List<PlannedClass> {
    pureDriverBuilder.build().use { driver ->
      driver.of(studio).setAllLocation()

      dates.forEach {
        driver.setDate(it).parse()
      }

      return driver.allClasses
    }
  }
}