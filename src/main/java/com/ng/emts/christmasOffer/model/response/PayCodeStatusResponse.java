package com.ng.emts.christmasOffer.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayCodeStatusResponse {
    private String channel;
    private String code;
    private String description;
    private String paymentMethodCode;
    private String paymentMethodTypeCode;
    private String tokenLifeTimeInMinutes;
    private String amount;
    private String subscriberId;
    private String status;
    private String frontEndPartner;
    private String transactionType;
}
