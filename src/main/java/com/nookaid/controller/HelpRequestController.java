
package com.nookaid.controller;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<HelpRequestResponse>> getAllRequests() {

        List<HelpRequestResponse> responses =
                helpRequestService.getAllRequests()
                        .stream()
                        .map(request -> new HelpRequestResponse(
                                request.getId(),
                                request.getTitle(),
                                request.getDescription(),
                                request.getCategory(),
                                request.getStatus(),
                                request.getUser().getId(),
                                request.getUser().getName(),
                                request.getCreatedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpRequestResponse> getRequestById(
            @PathVariable Long id
    ) {

        var request = helpRequestService.getRequestById(id);

        HelpRequestResponse response = new HelpRequestResponse(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getStatus(),
                request.getUser().getId(),
                request.getUser().getName(),
                request.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<HelpRequestResponse>> getRequestsByStatus(
            @RequestParam String status
    ) {

        List<HelpRequestResponse> responses =
                helpRequestService.getRequestsByStatus(status)
                        .stream()
                        .map(request -> new HelpRequestResponse(
                                request.getId(),
                                request.getTitle(),
                                request.getDescription(),
                                request.getCategory(),
                                request.getStatus(),
                                request.getUser().getId(),
                                request.getUser().getName(),
                                request.getCreatedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HelpRequestResponse> updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody HelpRequestRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        HelpRequest updatedRequest =
                helpRequestService.updateRequest(id, request, email);

        HelpRequestResponse response = new HelpRequestResponse(
                updatedRequest.getId(),
                updatedRequest.getTitle(),
                updatedRequest.getDescription(),
                updatedRequest.getCategory(),
                updatedRequest.getStatus(),
                updatedRequest.getUser().getId(),
                updatedRequest.getUser().getName(),
                updatedRequest.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email = authentication.getName();

        helpRequestService.deleteRequest(id, email);

        return ResponseEntity.ok("Help request deleted successfully");
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<HelpRequestResponse>> getMyRequests(
            Authentication authentication
    ) {

        String email = authentication.getName();

        List<HelpRequestResponse> responses =
                helpRequestService.getMyRequests(email)
                        .stream()
                        .map(request -> new HelpRequestResponse(
                                request.getId(),
                                request.getTitle(),
                                request.getDescription(),
                                request.getCategory(),
                                request.getStatus(),
                                request.getUser().getId(),
                                request.getUser().getName(),
                                request.getCreatedAt()
                        ))
                        .toList();

        return ResponseEntity.ok(responses);
    }


}
