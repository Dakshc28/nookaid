
package com.nookaid.service;

import com.nookaid.dto.HelpOfferRequest;
import com.nookaid.entity.HelpOffer;
import com.nookaid.entity.HelpRequest;
import com.nookaid.entity.User;
import com.nookaid.repository.HelpOfferRepository;
import com.nookaid.repository.HelpRequestRepository;
import com.nookaid.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpOfferService {

    private final HelpOfferRepository helpOfferRepository;
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    public HelpOfferService(
            HelpOfferRepository helpOfferRepository,
            HelpRequestRepository helpRequestRepository,
            UserRepository userRepository
    ) {
        this.helpOfferRepository = helpOfferRepository;
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
    }

    public HelpOffer createOffer(
            Long requestId,
            HelpOfferRequest request,
            String email
    ) {

        HelpRequest helpRequest = helpRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Help request not found"));

        User helper = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (helpRequest.getUser().getId().equals(helper.getId())) {
            throw new AccessDeniedException(
                    "You cannot offer help on your own request");
        }

        if (!"OPEN".equals(helpRequest.getStatus())) {
            throw new IllegalArgumentException(
                    "You can only offer help on OPEN requests");
        }

        boolean alreadyOffered =
                helpOfferRepository.existsByHelpRequestIdAndHelperId(
                        requestId, helper.getId());

        if (alreadyOffered) {
            throw new IllegalArgumentException(
                    "You have already offered help on this request");
        }

        HelpOffer offer = new HelpOffer();
        offer.setHelpRequest(helpRequest);
        offer.setHelper(helper);
        offer.setMessage(request.getMessage());
        offer.setStatus("PENDING");

        return helpOfferRepository.save(offer);
    }

    public List<HelpOffer> getOffersForRequest(Long requestId, String email) {

        HelpRequest helpRequest = helpRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Help request not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!helpRequest.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to view offers for this request");
        }

        return helpOfferRepository.findByHelpRequestId(requestId);
    }

    public List<HelpOffer> getMyOffers(String email) {

        User helper = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return helpOfferRepository.findByHelperId(helper.getId());
    }

    public HelpOffer acceptOffer(Long offerId, String email) {

        HelpOffer offer = helpOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Offer not found"));

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        HelpRequest request = offer.getHelpRequest();

        if (!request.getUser().getId().equals(owner.getId())) {
            throw new AccessDeniedException(
                    "Only the request owner can accept an offer");
        }

        if (!"PENDING".equals(offer.getStatus())) {
            throw new IllegalArgumentException(
                    "This offer has already been processed");
        }

        if (!"OPEN".equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "This request is no longer open");
        }

        offer.setStatus("ACCEPTED");
        request.setStatus("IN_PROGRESS");

        helpRequestRepository.save(request);

        return helpOfferRepository.save(offer);
    }

    public HelpOffer rejectOffer(Long offerId, String email) {

        HelpOffer offer = helpOfferRepository.findById(offerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Offer not found"));

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!offer.getHelpRequest().getUser().getId()
                .equals(owner.getId())) {
            throw new AccessDeniedException(
                    "Only the request owner can reject an offer");
        }

        if (!"PENDING".equals(offer.getStatus())) {
            throw new IllegalArgumentException(
                    "This offer has already been processed");
        }

        offer.setStatus("REJECTED");

        return helpOfferRepository.save(offer);
    }

}
