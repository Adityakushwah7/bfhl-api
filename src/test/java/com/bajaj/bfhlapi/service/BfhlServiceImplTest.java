package com.bajaj.bfhlapi.service;

import com.bajaj.bfhlapi.dto.BfhlRequest;
import com.bajaj.bfhlapi.dto.BfhlResponse;
import com.bajaj.bfhlapi.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BfhlServiceImpl.
 * Tests all business logic including number classification, alphabet extraction,
 * special character detection, sum calculation, and concat string generation.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
        // Inject test values for @Value fields
        ReflectionTestUtils.setField(bfhlService, "fullName", "aditya_kushwah");
        ReflectionTestUtils.setField(bfhlService, "email", "adityakushwah241283@acropolis.in");
        ReflectionTestUtils.setField(bfhlService, "rollNumber", "0827IT243D01");
        ReflectionTestUtils.setField(bfhlService, "dob", "16122004");
    }

    // =========================================================================
    // Example A: ["M", "1", "334", "4", "R", "9"]
    // =========================================================================
    @Test
    @DisplayName("Example A: Mixed numbers and alphabets")
    void testExampleA() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("M", "1", "334", "4", "R", "9"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals("aditya_kushwah_16122004", response.getUserId());
        assertEquals("adityakushwah241283@acropolis.in", response.getEmail());
        assertEquals("0827IT243D01", response.getRollNumber());

        // Odd numbers: 1, 9
        assertEquals(Arrays.asList("1", "9"), response.getOddNumbers());

        // Even numbers: 334, 4
        assertEquals(Arrays.asList("334", "4"), response.getEvenNumbers());

        // Alphabets: M, R
        assertEquals(Arrays.asList("M", "R"), response.getAlphabets());

        // Special characters: none
        assertTrue(response.getSpecialCharacters().isEmpty());

        // Sum: 1 + 334 + 4 + 9 = 348
        assertEquals("348", response.getSum());

        // Concat: [M, R] → reversed [R, M] → alternating caps "Rm"
        assertEquals("Rm", response.getConcatString());
    }

    // =========================================================================
    // Example B: ["2", "a", "J", "4", "N", ",", "1", "9", "42", "Y"]
    // =========================================================================
    @Test
    @DisplayName("Example B: Numbers, alphabets, and special characters")
    void testExampleB() {
        BfhlRequest request = new BfhlRequest(
                Arrays.asList("2", "a", "J", "4", "N", ",", "1", "9", "42", "Y"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());

        // Odd numbers: 1, 9
        assertEquals(Arrays.asList("1", "9"), response.getOddNumbers());

        // Even numbers: 2, 4, 42
        assertEquals(Arrays.asList("2", "4", "42"), response.getEvenNumbers());

        // Alphabets: a, J, N, Y
        assertEquals(Arrays.asList("a", "J", "N", "Y"), response.getAlphabets());

        // Special characters: ,
        assertEquals(Arrays.asList(","), response.getSpecialCharacters());

        // Sum: 2 + 4 + 1 + 9 + 42 = 58
        assertEquals("58", response.getSum());

        // Concat: [a, J, N, Y] → reversed [Y, N, J, a] → alternating caps "YnJa"
        assertEquals("YnJa", response.getConcatString());
    }

    // =========================================================================
    // Example C: ["X", "ABCD", "DGE"]
    // =========================================================================
    @Test
    @DisplayName("Example C: Multi-character alphabetical entries")
    void testExampleC() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("X", "ABCD", "DGE"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());

        // No numbers
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());

        // Alphabets: X, ABCD, DGE
        assertEquals(Arrays.asList("X", "ABCD", "DGE"), response.getAlphabets());

        // No special characters
        assertTrue(response.getSpecialCharacters().isEmpty());

        // Sum: 0
        assertEquals("0", response.getSum());

        // Concat: chars [X, A, B, C, D, D, G, E] → reversed [E, G, D, D, C, B, A, X]
        // → alternating caps "EgDdCbAx"
        assertEquals("EgDdCbAx", response.getConcatString());
    }

    // =========================================================================
    // Edge Cases
    // =========================================================================
    @Test
    @DisplayName("Empty data array")
    void testEmptyData() {
        BfhlRequest request = new BfhlRequest(Collections.emptyList());
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Only numbers")
    void testOnlyNumbers() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("1", "2", "3", "4", "5"));
        BfhlResponse response = bfhlService.processData(request);

        assertEquals(Arrays.asList("1", "3", "5"), response.getOddNumbers());
        assertEquals(Arrays.asList("2", "4"), response.getEvenNumbers());
        assertTrue(response.getAlphabets().isEmpty());
        assertEquals("15", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Only alphabets")
    void testOnlyAlphabets() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "B", "c", "D"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertEquals(Arrays.asList("a", "B", "c", "D"), response.getAlphabets());
        assertEquals("0", response.getSum());
        // Reversed: [D, c, B, a] → alternating caps: D(up), c(low), B(up), a(low) = "DcBa"
        assertEquals("DcBa", response.getConcatString());
    }

    @Test
    @DisplayName("Only special characters")
    void testOnlySpecialCharacters() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("@", "#", "$", "!"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertEquals(Arrays.asList("@", "#", "$", "!"), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    @DisplayName("Single alphabet character")
    void testSingleAlphabet() {
        BfhlRequest request = new BfhlRequest(Collections.singletonList("z"));
        BfhlResponse response = bfhlService.processData(request);

        assertEquals(Arrays.asList("z"), response.getAlphabets());
        // Reversed: [z] → alternating caps: Z (uppercase at index 0)
        assertEquals("Z", response.getConcatString());
    }

    @Test
    @DisplayName("Large numbers")
    void testLargeNumbers() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("1000000", "999999"));
        BfhlResponse response = bfhlService.processData(request);

        assertEquals(Arrays.asList("999999"), response.getOddNumbers());
        assertEquals(Arrays.asList("1000000"), response.getEvenNumbers());
        assertEquals("1999999", response.getSum());
    }

    @Test
    @DisplayName("Zero is even")
    void testZeroIsEven() {
        BfhlRequest request = new BfhlRequest(Collections.singletonList("0"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.getOddNumbers().isEmpty());
        assertEquals(Arrays.asList("0"), response.getEvenNumbers());
        assertEquals("0", response.getSum());
    }

    @Test
    @DisplayName("User ID format is correct")
    void testUserIdFormat() {
        BfhlRequest request = new BfhlRequest(Collections.singletonList("1"));
        BfhlResponse response = bfhlService.processData(request);

        assertEquals("aditya_kushwah_16122004", response.getUserId());
    }

    @Test
    @DisplayName("is_success is always true for valid input")
    void testIsSuccessTrue() {
        BfhlRequest request = new BfhlRequest(Arrays.asList("1", "a"));
        BfhlResponse response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
    }
}
