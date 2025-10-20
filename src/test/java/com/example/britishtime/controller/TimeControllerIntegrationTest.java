package com.example.britishtime.controller;

import com.example.britishtime.BritishSpokenTimeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    @Test
    @DisplayName("Should successfully process CSV file and return spoken times")
    void shouldProcessCsvFileSuccessfully() throws Exception {
        String csvData = "07:30,09:45,12:00\n00:00,01:15,05:20";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "times.csv",
                "text/csv",
                csvData.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/v1/spoken-time/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].original", is("07:30")))
                .andExpect(jsonPath("$[0].spoken", containsString("half past seven")))
                .andExpect(jsonPath("$[2].spoken", containsString("noon")))
                .andExpect(jsonPath("$[3].spoken", containsString("midnight")));
    }

    @Test
    @DisplayName("Should return error when CSV file is invalid or unreadable")
    void shouldReturnErrorForInvalidCsv() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "invalid.csv",
                "text/plain",
                "".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/v1/spoken-time/upload")
                        .file(invalidFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("invalid_file")))
                .andExpect(jsonPath("$.message", containsString("Invalid CSV format")));
    }
}
