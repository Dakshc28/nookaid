
package com.nookaid.dto;

import java.time.LocalDateTime;

public class HelpRequestResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String status;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;

    public HelpRequestResponse(
            Long id,
            String title,
            String description,
            String category,
            String status,
            Long userId,
            String userName,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
