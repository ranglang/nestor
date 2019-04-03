package org.gotson.nestor.infrastructure.selenium

import mu.KotlinLogging
import org.gotson.nestor.domain.model.BookingResult
import org.gotson.nestor.domain.model.CredentialsException
import org.gotson.nestor.domain.model.PlannedClass
import org.gotson.nestor.domain.model.PlannedClassBookingState
import org.gotson.nestor.domain.model.Studio
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.Closeable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

private val logger = KotlinLogging.logger {}

val tenDeletes = List<CharSequence>(10) { Keys.DELETE }.toTypedArray()
val tenLeft = List<CharSequence>(10) { Keys.LEFT }.toTypedArray()

class PureDriver(
    chromeOptions: ChromeOptions,
    waitTime: Long,
    timeFormat: String,
    dateFormat: String
) : Closeable {
  private val driver: ChromeDriver = ChromeDriver(chromeOptions)

  private val wait = WebDriverWait(driver, waitTime)
  private val formatterDateInput = DateTimeFormatter.ofPattern(dateFormat)
  private val formatterAmPmParser = DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .appendPattern(timeFormat)
      .toFormatter()
  private lateinit var date: LocalDate
  private lateinit var studio: Studio

  val allClasses = ArrayList<PlannedClass>()
  private val _bookableClasses = HashMap<PlannedClass, WebElement>()

  val bookableClasses: Set<PlannedClass>
    get() = _bookableClasses.keys

  fun performUserLogin(userName: String, password: String): PureDriver {
    val userLoginEl = driver.findElement(By.name("requiredtxtUserName"))
    val userPasswordEl = driver.findElement(By.name("requiredtxtPassword"))
    val btnLoginEl = driver.findElement(By.id("btnLogin"))

    userLoginEl.sendKeys(userName)
    userPasswordEl.sendKeys(password)
    btnLoginEl.click()

    try {
      val welcomeBanner = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("top-wel-sp")))
      logger.info { "Login successful: ${welcomeBanner.text}" }
    } catch (e: Exception) {
      throw CredentialsException("Failed to log in", e)
    }

    return this
  }

  fun of(studio: Studio): PureDriver {
    driver.get(studio.url)
    this.studio = studio

    return this
  }

  fun setAllLocation(): PureDriver {
    val locationEl = driver.findElement(By.cssSelector("a.selectBox"))
    locationEl.click()

    // not used - specific selector for one location
    // By.cssSelector("li[title='$loc']")
    val selector = By.cssSelector("[rel='0']")

    val dropdownLocationItemEl = wait.until(ExpectedConditions.presenceOfElementLocated(selector))
    dropdownLocationItemEl.click()

    return this
  }

  fun setDate(date: LocalDate): PureDriver {
    val previousDateEl = driver.findElement(By.id("txtDate"))
    val previousDateValue = previousDateEl.getAttribute("value")
    val previousDate = LocalDate.parse(previousDateValue, formatterDateInput)

    previousDateEl.sendKeys(*tenLeft, *tenDeletes, date.format(formatterDateInput))
    previousDateEl.submit()

    val newDateEl = driver.findElement(By.id("txtDate"))
    val newDateValue = newDateEl.getAttribute("value")
    val newDate = LocalDate.parse(newDateValue, formatterDateInput)

    logger.info { "Wished date: $date, previous date: $previousDate, new date: $newDate" }
    if (newDate != date)
      throw Exception("Could not change date to $date")

    this.date = date

    return this
  }

  fun parse(): PureDriver {
    val classTableEl = driver.findElements(By.cssSelector(".classSchedule-mainTable-loaded tbody tr"))

    classTableEl.forEach {
      val rowEl = it.findElements(By.cssSelector("td"))
      // skip day of week table element
      if (rowEl.size < 2) return@forEach

      val classTime = LocalTime.parse(rowEl[0].text.trim(), formatterAmPmParser)
      val classType = rowEl[2].text
      val classInstructor = rowEl[3].text
      val classLocation = rowEl[5].text
      var signUpButtonEl: WebElement? = null
      val classBookableState =
          if (rowEl[1].text.contains("registered", ignoreCase = true))
            PlannedClassBookingState.REGISTERED
          else {
            val elements = rowEl[1].findElements(By.className("SignupButton"))
            if (elements.size > 0 && elements[0].getAttribute("value").contains("sign up now", ignoreCase = true)) {
              signUpButtonEl = elements[0]
              PlannedClassBookingState.OPEN
            } else
              PlannedClassBookingState.CLOSED
          }

      val parsedClass = PlannedClass(
          date = date,
          time = classTime,
          type = classType,
          instructor = classInstructor,
          location = classLocation,
          bookingState = classBookableState,
          studio = studio)

      allClasses += parsedClass
      if (parsedClass.bookingState == PlannedClassBookingState.OPEN && signUpButtonEl != null)
        _bookableClasses[parsedClass] = signUpButtonEl
    }

    logger.info { "All registered classes:" }
    allClasses.filter { it.bookingState == PlannedClassBookingState.REGISTERED }.forEach { logger.info { it } }

    logger.info { "All available classes:" }
    allClasses.filter { it.bookingState == PlannedClassBookingState.OPEN }.forEach { logger.info { it } }

    logger.info { "All unavailable classes:" }
    allClasses.filter { it.bookingState == PlannedClassBookingState.CLOSED }.forEach { logger.info { it } }

    return this
  }

  fun book(plannedClass: PlannedClass): BookingResult {
    val el = _bookableClasses[plannedClass] ?: throw Exception("No webElement found for class: $plannedClass")

    el.click()

    val bookButtonEl = driver.findElements(By.id("SubmitEnroll2"))
    val waitListButtonEl = driver.findElements(By.name("AddWLButton"))

    return when {
      bookButtonEl.isNotEmpty() -> {
        bookButtonEl.first().click()
        logger.info { "Booked class!" }
        BookingResult.BOOKED
      }
      waitListButtonEl.isNotEmpty() -> {
        waitListButtonEl.first().click()
        logger.info { "Booked on waitlist!" }
        BookingResult.WAITLIST
      }
      else -> {
        logger.error { "Ooops, could not book nor waitlist" }
        BookingResult.ERROR
      }
    }
  }

  override fun close() {
    driver.close()
  }
}
