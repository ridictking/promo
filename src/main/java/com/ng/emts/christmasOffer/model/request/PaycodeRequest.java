package com.ng.emts.christmasOffer.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaycodeRequest {
    private	String amount;
    private	String ttid ;
    private String msisdn;
    private	String frontEndPartnerId;
    private	String paymentMethodTypeCode;
    private	String paymentMethodCode;
    private	String payWithMobileChannel;
    private	String tokenLifeTimeInMinutes;
    private	String oneTimePin;
    private	String codeGenerationChannelProvider;
    private	String codeGenerationChannel;
    private	String transactionRef;
    private String clientId;
    private String clientSecret;
    private String payCode;

}

