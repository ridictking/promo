package com.ng.emts.christmasOffer.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaycodeResponse {
    private String subscriberId;
    private String payWithMobileToken;
    private String tokenLifeTimeInMinutes;
    private String balance;
    private String maxCount;
    private String currentCount;
    private String isReusable;
}
