
package com.nookaid.repository;

import com.nookaid.entity.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRequestRepository
        extends JpaRepository<HelpRequest, Long> {

    List<HelpRequest> findByStatus(String status);

    List<HelpRequest> findByUserId(Long userId);
}
