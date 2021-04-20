package com.ng.emts.christmasOffer.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayCodeAuthorizeResponse {
    private String responseCode;
    private String responseDescription;
}
