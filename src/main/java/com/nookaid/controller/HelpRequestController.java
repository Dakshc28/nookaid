
package com.nookaid.controller;

import com.nookaid.dto.HelpRequestRequest;
import com.nookaid.dto.HelpRequestResponse;
import com.nookaid.entity.HelpRequest;
import com.nookaid.service.HelpRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    @PostMapping
    public ResponseEntity<HelpRequestResponse> createRequest(
            @Valid @RequestBody HelpRequestRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        HelpRequest savedRequest =
                helpRequestService.createRequest(request, email);

        HelpRequestResponse response = new HelpRequestResponse(
                savedRequest.getId(),
                savedRequest.getTitle(),
                savedRequest.getDescription(),
                savedRequest.getCategory(),
                savedRequest.getStatus(),
                savedRequest.getUser().getId(),
                savedRequest.getUser().getName(),
                savedRequest.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
