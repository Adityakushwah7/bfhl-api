package com.bajaj.bfhlapi.service;

import com.bajaj.bfhlapi.dto.BfhlRequest;
import com.bajaj.bfhlapi.dto.BfhlResponse;

/**
 * Service interface for processing BFHL data.
 * Provides abstraction over the business logic layer.
 */
public interface BfhlService {

    /**
     * Processes the input data array and returns categorized results.
     *
     * @param request the request containing the data array
     * @return BfhlResponse with categorized numbers, alphabets, special characters,
     *         sum, and concatenated string
     */
    BfhlResponse processData(BfhlRequest request);
}
