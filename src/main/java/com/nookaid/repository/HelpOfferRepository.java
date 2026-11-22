
package com.nookaid.repository;

import com.nookaid.entity.HelpOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long> {

    List<HelpOffer> findByHelpRequestId(Long requestId);

    List<HelpOffer> findByHelperId(Long helperId);
    boolean existsByHelpRequestIdAndHelperId(Long requestId, Long helperId);
}
