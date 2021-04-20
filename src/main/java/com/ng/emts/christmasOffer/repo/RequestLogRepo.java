package com.ng.emts.christmasOffer.repo;


import com.ng.emts.christmasOffer.model.data.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RequestLogRepo extends JpaRepository<RequestLog, Long> {
    RequestLog findByRequestInTimeAndMsisdn(LocalDateTime requestTimeIn, String msisdn);
}
