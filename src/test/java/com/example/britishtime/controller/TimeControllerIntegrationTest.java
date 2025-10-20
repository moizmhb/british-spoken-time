package com.example.britishtime.controller;

import com.example.britishtime.BritishSpokenTimeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BritishSpokenTimeApplication.class)
@AutoConfigureMockMvc
class TimeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnNoonFor12_00() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time")
                        .param("time", "12:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spoken", is("noon")));
    }

    @Test
    void shouldReturnQuarterToTenFor9_45() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time/9/45"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spoken", is("quarter to ten")));
    }

    @Test
    void shouldReturnHalfPastSevenFor7_30() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time")
                        .param("time", "07:30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spoken", is("half past seven")));
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time").param("time", "25:99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("invalid_time")));
    }

    @Test
    void shouldReturnMidnightFor00_00() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time?time=00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spoken", is("midnight")));
    }

    @Test
    void shouldReturnSixThirtyTwoFor6_32() throws Exception {
        mockMvc.perform(get("/api/v1/spoken-time/6/32"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spoken", is("six thirty two")));
    }
}
