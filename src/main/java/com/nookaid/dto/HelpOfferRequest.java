
package com.nookaid.dto;

import jakarta.validation.constraints.Size;

public class HelpOfferRequest {

    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
