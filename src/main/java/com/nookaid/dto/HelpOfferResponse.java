
package com.nookaid.dto;

import java.time.LocalDateTime;

public class HelpOfferResponse {

    private Long id;
    private Long requestId;
    private Long helperId;
    private String helperName;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public HelpOfferResponse(
            Long id,
            Long requestId,
            Long helperId,
            String helperName,
            String message,
            String status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.requestId = requestId;
        this.helperId = helperId;
        this.helperName = helperName;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getRequestId() { return requestId; }
    public Long getHelperId() { return helperId; }
    public String getHelperName() { return helperName; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
