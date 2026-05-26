package com.bajaj.bfhlapi.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO for the /bfhl POST endpoint.
 * Contains a list of string data elements to be processed.
 */
public class BfhlRequest {

    @NotNull(message = "Data field cannot be null")
    private List<String> data;

    public BfhlRequest() {
    }

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
