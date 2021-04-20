package com.ng.emts.christmasOffer.repo;


import com.ng.emts.christmasOffer.model.data.PaycodeAuthorizeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayCodeAuthorizeRequestRepo extends JpaRepository<PaycodeAuthorizeRequest, Long> {
}
