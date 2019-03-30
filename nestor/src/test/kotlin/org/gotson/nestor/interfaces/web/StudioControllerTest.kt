package org.gotson.nestor.interfaces.web

import org.gotson.nestor.domain.model.Studio
import org.gotson.nestor.domain.persistence.StudioRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class StudioControllerTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var studioRepository: StudioRepository

    private val pureYoga = Studio(1, "Pure Yoga", "http://pureyoga.com")
    private val pureFitness = Studio(2, "Pure Fitness", "http://purefitness.com")

    private val route = "/studio"

    @Test
    fun `given no studio when getAll then return empty list`() {
        given(studioRepository.findAll()).willReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get(route))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Any>(0)))
    }

    @Test
    fun `given a studio when getAll then return single studio in list`() {
        given(studioRepository.findAll()).willReturn(listOf(pureYoga))

        mockMvc.perform(MockMvcRequestBuilders.get(route))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize<Any>(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.equalTo(pureYoga.name)))
    }

    @Test
    fun `given incorrect id when getOne then return error not found`() {
        given(studioRepository.findById(any())).willReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.get("$route/12"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `given studio id when getOne then return studio`() {
        given(studioRepository.findById(2)).willReturn(Optional.of(pureFitness))

        mockMvc.perform(MockMvcRequestBuilders.get("$route/2"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(pureFitness.name)))
    }

    @Test
    fun `given studio without name when addOne then return bad request`() {
        val jsonString = """{"url":"http://pureyoga.com"}"""

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given studio with blank name when addOne then return bad request`() {
        val jsonString = """{"url":"http://pureyoga.com", "name":""}"""

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given studio without url when addOne then return bad request`() {
        val jsonString = """{"name":"Pure Yoga"}"""

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given studio with malformed url when addOne then return bad request`() {
        val jsonString = """{"url":"pureyoga", "name":"Pure Yoga"}"""

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given studio with url already existing when addOne then return bad request`() {
        val url = "http://pureyoga.com"
        val jsonString = """{"url":"$url", "name":"Pure Yoga"}"""

        given(studioRepository.existsByUrl(url)).willReturn(true)

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given valid studio when addOne then return studio`() {
        val jsonString = """{"url":"http://pureyoga.com", "name":"Pure Yoga"}"""

        mockMvc.perform(MockMvcRequestBuilders.post(route)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo("Pure Yoga")))
    }
}