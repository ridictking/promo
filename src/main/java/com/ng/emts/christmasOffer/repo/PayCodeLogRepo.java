package com.ng.emts.christmasOffer.repo;

import com.ng.emts.christmasOffer.model.data.PayCodeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayCodeLogRepo  extends JpaRepository<PayCodeLog, Long> {
    PayCodeLog findByPayCode(String payCode);
}
