package com.bajaj.bfhlapi.controller;

import com.bajaj.bfhlapi.dto.BfhlRequest;
import com.bajaj.bfhlapi.dto.BfhlResponse;
import com.bajaj.bfhlapi.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for the /bfhl endpoint.
 * Handles POST requests to process data arrays.
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl
     * Accepts a JSON body with a "data" array and returns categorized results.
     *
     * @param request the request body containing the data array
     * @return 200 OK with the processed response
     */
    @PostMapping
    public ResponseEntity<BfhlResponse> processData(@Valid @RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /bfhl
     * Returns a simple operation code (optional, for health check).
     */
    @GetMapping
    public ResponseEntity<OperationCodeResponse> getOperationCode() {
        return ResponseEntity.ok(new OperationCodeResponse(1));
    }

    /**
     * Simple response for GET endpoint.
     */
    public static class OperationCodeResponse {
        private int operation_code;

        public OperationCodeResponse(int operationCode) {
            this.operation_code = operationCode;
        }

        public int getOperation_code() {
            return operation_code;
        }

        public void setOperation_code(int operation_code) {
            this.operation_code = operation_code;
        }
    }
}
