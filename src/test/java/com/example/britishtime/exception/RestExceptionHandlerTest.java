package com.example.britishtime.exception;

import com.example.britishtime.controller.TimeController;
import com.example.britishtime.service.TimeService;
import com.example.britishtime.formatter.TimeFormatterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TimeController.class)
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeService timeService;

    @MockBean
    private TimeFormatterFactory factory;

    @BeforeEach
    void setup() {
        // Default: throw nothing unless specifically stubbed
    }

    @Test
    void shouldReturnBadRequestForInvalidTimeException() throws Exception {
        when(timeService.toSpokenTime("25:99"))
                .thenThrow(new InvalidTimeException("hour must be between 0 and 23"));

        mockMvc.perform(get("/api/v1/spoken-time")
                        .param("time", "25:99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("invalid_time")))
                .andExpect(jsonPath("$.message", containsString("hour must be between 0 and 23")));
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedException() throws Exception {
        when(timeService.toSpokenTime("12:12"))
                .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/api/v1/spoken-time")
                        .param("time", "12:12")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code", is("server_error")))
                .andExpect(jsonPath("$.message", containsString("Unexpected failure")));
    }
}
