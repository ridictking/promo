package com.ng.emts.christmasOffer.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IswHeaders {
    private String authorization;
    private long timeStamp;
    private String signatureMethod;
    private String nonce;
    private String signature;
    private String frontEndPartnerId;
    private String accessToken;
}
