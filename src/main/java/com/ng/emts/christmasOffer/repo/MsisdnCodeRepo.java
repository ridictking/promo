package com.ng.emts.christmasOffer.repo;


import com.ng.emts.christmasOffer.model.data.MsisdnLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsisdnCodeRepo extends JpaRepository<MsisdnLog, Long> {
    MsisdnLog findByMsisdn(String msisdn);
}
