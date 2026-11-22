
package com.nookaid.controller;

import com.nookaid.dto.HelpOfferRequest;
import com.nookaid.dto.HelpOfferResponse;
import com.nookaid.entity.HelpOffer;
import com.nookaid.service.HelpOfferService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HelpOfferController {

    private final HelpOfferService helpOfferService;

    public HelpOfferController(HelpOfferService helpOfferService) {
        this.helpOfferService = helpOfferService;
    }

    @PostMapping("/requests/{requestId}/offers")
    public ResponseEntity<HelpOfferResponse> createOffer(
            @PathVariable Long requestId,
            @Valid @RequestBody HelpOfferRequest request,
            Authentication authentication
    ) {
        HelpOffer offer = helpOfferService.createOffer(
                requestId, request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToResponse(offer));
    }

    @GetMapping("/requests/{requestId}/offers")
    public ResponseEntity<List<HelpOfferResponse>> getOffersForRequest(
            @PathVariable Long requestId,
            Authentication authentication
    ) {
        List<HelpOfferResponse> responses =
                helpOfferService.getOffersForRequest(
                                requestId, authentication.getName())
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/offers/my-offers")
    public ResponseEntity<List<HelpOfferResponse>> getMyOffers(
            Authentication authentication
    ) {
        List<HelpOfferResponse> responses =
                helpOfferService.getMyOffers(authentication.getName())
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    private HelpOfferResponse convertToResponse(HelpOffer offer) {
        return new HelpOfferResponse(
                offer.getId(),
                offer.getHelpRequest().getId(),
                offer.getHelper().getId(),
                offer.getHelper().getName(),
                offer.getMessage(),
                offer.getStatus(),
                offer.getCreatedAt()
        );
    }

    @PutMapping("/offers/{offerId}/accept")
    public ResponseEntity<HelpOfferResponse> acceptOffer(
            @PathVariable Long offerId,
            Authentication authentication
    ) {
        HelpOffer offer = helpOfferService.acceptOffer(
                offerId, authentication.getName());

        return ResponseEntity.ok(convertToResponse(offer));
    }

    @PutMapping("/offers/{offerId}/reject")
    public ResponseEntity<HelpOfferResponse> rejectOffer(
            @PathVariable Long offerId,
            Authentication authentication
    ) {
        HelpOffer offer = helpOfferService.rejectOffer(
                offerId, authentication.getName());

        return ResponseEntity.ok(convertToResponse(offer));
    }

}
