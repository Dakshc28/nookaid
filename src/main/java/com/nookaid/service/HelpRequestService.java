
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

}
