package com.ng.emts.christmasOffer.repo;


import com.ng.emts.christmasOffer.model.data.ChristmasOfferRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChristmasOfferRepo extends JpaRepository<ChristmasOfferRequestLog, Long> {
    ChristmasOfferRequestLog findByMsisdn(String msisdn);
}
