package com.bajaj.bfhlapi.controller;

import com.bajaj.bfhlapi.dto.BfhlRequest;
import com.bajaj.bfhlapi.dto.BfhlResponse;
import com.bajaj.bfhlapi.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BfhlController.
 * Tests HTTP layer including request/response serialization and status codes.
 */
@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BfhlService bfhlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /bfhl returns 200 with valid request")
    void testPostBfhl_ValidRequest_Returns200() throws Exception {
        // Arrange
        BfhlResponse mockResponse = BfhlResponse.builder()
                .isSuccess(true)
                .userId("aditya_kushwah_16122004")
                .email("adityakushwah241283@acropolis.in")
                .rollNumber("0827IT243D01")
                .oddNumbers(Arrays.asList("1", "9"))
                .evenNumbers(Arrays.asList("334", "4"))
                .alphabets(Arrays.asList("M", "R"))
                .specialCharacters(Collections.emptyList())
                .sum("348")
                .concatString("Rm")
                .build();

        when(bfhlService.processData(any(BfhlRequest.class))).thenReturn(mockResponse);

        String requestJson = "{\"data\": [\"M\", \"1\", \"334\", \"4\", \"R\", \"9\"]}";

        // Act & Assert
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("aditya_kushwah_16122004"))
                .andExpect(jsonPath("$.email").value("adityakushwah241283@acropolis.in"))
                .andExpect(jsonPath("$.roll_number").value("0827IT243D01"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.odd_numbers[1]").value("9"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.even_numbers[1]").value("4"))
                .andExpect(jsonPath("$.alphabets[0]").value("M"))
                .andExpect(jsonPath("$.alphabets[1]").value("R"))
                .andExpect(jsonPath("$.special_characters").isEmpty())
                .andExpect(jsonPath("$.sum").value("348"))
                .andExpect(jsonPath("$.concat_string").value("Rm"));
    }

    @Test
    @DisplayName("POST /bfhl with null data returns 400")
    void testPostBfhl_NullData_Returns400() throws Exception {
        String requestJson = "{}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bfhl with invalid JSON returns 400")
    void testPostBfhl_InvalidJson_Returns400() throws Exception {
        String requestJson = "invalid json";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("GET /bfhl returns operation code 1")
    void testGetBfhl_Returns200WithOperationCode() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code").value(1));
    }
}
