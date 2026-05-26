package com.bajaj.bfhlapi.service.impl;

import com.bajaj.bfhlapi.dto.BfhlRequest;
import com.bajaj.bfhlapi.dto.BfhlResponse;
import com.bajaj.bfhlapi.service.BfhlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of BfhlService.
 * Contains the core business logic for processing input data arrays.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${app.user.full-name}")
    private String fullName;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.roll-number}")
    private String rollNumber;

    @Value("${app.user.dob}")
    private String dob;

    @Override
    public BfhlResponse processData(BfhlRequest request) {
        List<String> data = request.getData();

        List<String> evenNumbers = new ArrayList<>();
        List<String> oddNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long sum = 0;

        for (String item : data) {
            if (item == null || item.isEmpty()) {
                continue;
            }

            if (isNumeric(item)) {
                // It's a number
                long number = Long.parseLong(item);
                sum += number;
                if (number % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabetic(item)) {
                // It's purely alphabetic
                alphabets.add(item);
            } else {
                // It's a special character or mixed content
                specialCharacters.add(item);
            }
        }

        // Build concat_string: extract all alpha chars, reverse, alternating caps
        String concatString = buildConcatString(alphabets);

        // Build user_id in format: fullname_dob (e.g., john_doe_17091999)
        String userId = fullName.toLowerCase().replace(" ", "_") + "_" + dob;

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(String.valueOf(sum))
                .concatString(concatString)
                .build();
    }

    /**
     * Checks if a string represents a valid integer number.
     * Supports negative numbers as well.
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string contains only alphabetic characters.
     */
    private boolean isAlphabetic(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Builds the concatenation string by:
     * 1. Extracting all individual alphabetical characters from the alphabets list
     * 2. Reversing the order of characters
     * 3. Applying alternating caps (starting with uppercase)
     *
     * Example: ["M", "R"] → chars [M, R] → reversed [R, M] → alternating caps "Rm"
     * Example: ["a", "J", "N", "Y"] → reversed [Y, N, J, a] → "YnJa"
     */
    private String buildConcatString(List<String> alphabets) {
        if (alphabets == null || alphabets.isEmpty()) {
            return "";
        }

        // Extract all individual characters from alphabetical entries
        List<Character> allChars = new ArrayList<>();
        for (String alpha : alphabets) {
            for (char c : alpha.toCharArray()) {
                allChars.add(c);
            }
        }

        // Reverse the list
        Collections.reverse(allChars);

        // Apply alternating caps: index 0 = uppercase, index 1 = lowercase, ...
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allChars.size(); i++) {
            char c = allChars.get(i);
            if (i % 2 == 0) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }
}
