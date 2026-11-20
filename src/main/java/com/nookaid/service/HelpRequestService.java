
package com.nookaid.service;
import java.util.List;
import java.util.Optional;
import com.nookaid.dto.HelpRequestRequest;
import com.nookaid.entity.HelpRequest;
import com.nookaid.entity.User;
import com.nookaid.repository.HelpRequestRepository;
import com.nookaid.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    public HelpRequestService(
            HelpRequestRepository helpRequestRepository,
            UserRepository userRepository
    ) {
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
    }

    public HelpRequest createRequest(
            HelpRequestRequest request,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        HelpRequest helpRequest = new HelpRequest();

        helpRequest.setTitle(request.getTitle());
        helpRequest.setDescription(request.getDescription());
        helpRequest.setCategory(request.getCategory());
        helpRequest.setStatus("OPEN");
        helpRequest.setUser(user);

        return helpRequestRepository.save(helpRequest);
    }
    public List<HelpRequest> getAllRequests() {
        return helpRequestRepository.findAll();
    }

    public HelpRequest getRequestById(Long id) {
        return helpRequestRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Help request not found with ID: " + id
                        )
                );
    }

    public List<HelpRequest> getRequestsByStatus(String status) {
        return helpRequestRepository.findByStatus(status.toUpperCase());
    }

    public HelpRequest updateRequest(
            Long id,
            HelpRequestRequest request,
            String email
    ) {

        // Find the existing help request
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Help request not found with ID: " + id
                        )
                );

        // Find the currently logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        // Check whether the logged-in user owns this request
        if (!helpRequest.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to update this request"
            );
        }

        // Update request details
        helpRequest.setTitle(request.getTitle());
        helpRequest.setDescription(request.getDescription());
        helpRequest.setCategory(request.getCategory());

        return helpRequestRepository.save(helpRequest);
    }


    public void deleteRequest(Long id, String email) {

        // Find the request
        HelpRequest helpRequest = helpRequestRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Help request not found with ID: " + id
                        )
                );

        // Find the logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        // Check ownership
        if (!helpRequest.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to delete this request"
            );
        }

        // Delete the request
        helpRequestRepository.delete(helpRequest);
    }

    public List<HelpRequest> getMyRequests(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        return helpRequestRepository.findByUserId(user.getId());
    }


}
