package org.gotson.nestor.interfaces.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.persistence.StudioRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class StudioControllerTest(
    @Autowired private val mockMvc: MockMvc
) {
  @MockkBean
  private lateinit var studioRepository: StudioRepository

  private val pureYoga = Studio(1, "Pure Yoga", "http://pureyoga.com")
  private val pureFitness = Studio(2, "Pure Fitness", "http://purefitness.com")

  private val route = "/studio"

  @Nested
  inner class GetAll {
    @Test
    fun `given no studio when getAll then return empty list`() {
      every { studioRepository.findAll() } returns emptyList()

      mockMvc.perform(MockMvcRequestBuilders.get(route))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$", IsCollectionWithSize.hasSize<Any>(0)))
    }

    @Test
    fun `given a studio when getAll then return single studio in list`() {
      every { studioRepository.findAll() } returns listOf(pureYoga)

      mockMvc.perform(MockMvcRequestBuilders.get(route))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$", IsCollectionWithSize.hasSize<Any>(1)))
          .andExpect(jsonPath("$[0].name", equalTo(pureYoga.name)))
    }
  }

  @Nested
  inner class GetOne {
    @Test
    fun `given incorrect id when getOne then return error not found`() {
      every { studioRepository.findById(any()) } returns Optional.empty()

      mockMvc.perform(MockMvcRequestBuilders.get("$route/12"))
          .andExpect(status().isNotFound)
    }

    @Test
    fun `given studio id when getOne then return studio`() {
      every { studioRepository.findById(2) } returns Optional.of(pureFitness)

      mockMvc.perform(MockMvcRequestBuilders.get("$route/2"))
          .andExpect(status().isOk)
          .andExpect(jsonPath("$.name", equalTo(pureFitness.name)))
    }
  }

  @Nested
  inner class AddOne {
    @Test
    fun `given studio without name when addOne then return bad request`() {
      val jsonString = """{"url":"http://pureyoga.com"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given studio with blank name when addOne then return bad request`() {
      val jsonString = """{"url":"http://pureyoga.com", "name":""}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given studio without url when addOne then return bad request`() {
      val jsonString = """{"name":"Pure Yoga"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given studio with malformed url when addOne then return bad request`() {
      val jsonString = """{"url":"pureyoga", "name":"Pure Yoga"}"""

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given studio with url already existing when addOne then return bad request`() {
      val url = "http://pureyoga.com"
      val jsonString = """{"url":"$url", "name":"Pure Yoga"}"""

      every { studioRepository.existsByUrl(url) } returns true

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isBadRequest)
    }

    @Test
    fun `given valid studio when addOne then return studio`() {
      val url = "http://pureyoga.com"
      val jsonString = """{"url":"$url", "name":"Pure Yoga"}"""

      every { studioRepository.existsByUrl(url) } returns false
      every { studioRepository.save(ofType(Studio::class)) } returns Studio(1, "Pure Yoga", url)

      mockMvc.perform(MockMvcRequestBuilders.post(route)
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonString))
          .andExpect(status().isCreated)
          .andExpect(jsonPath("$.name", equalTo("Pure Yoga")))
    }
  }
}