package com.ng.emts.christmasOffer.api;

import com.ng.emts.christmasOffer.model.data.PayCodeAuthorizeResponse;
import com.ng.emts.christmasOffer.model.data.PayCodeReversalRequest;
import com.ng.emts.christmasOffer.model.data.PaycodeAuthorizeRequest;
import com.ng.emts.christmasOffer.service.isw.PaycodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class PayCodeApi {
    private final PaycodeService paycodeService;

    @Autowired
    public PayCodeApi(PaycodeService paycodeService) {
        this.paycodeService = paycodeService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<PayCodeAuthorizeResponse> authorize(@RequestBody PaycodeAuthorizeRequest paycodeAuthorizeRequest){
        log.info("Incoming request on AUTHORIZE ENDPOINT " + paycodeAuthorizeRequest.toString());
        PayCodeAuthorizeResponse authorize = paycodeService.authorize(paycodeAuthorizeRequest);
        ResponseEntity<PayCodeAuthorizeResponse> response;
        return getPayCodeAuthorizeResponseResponseEntity(authorize);
    }
    @PostMapping("/reverse")
    public ResponseEntity<PayCodeAuthorizeResponse> reversal(@RequestBody PayCodeReversalRequest payCodeReversalRequest){
        log.info("Incoming request on AUTHORIZE ENDPOINT " + payCodeReversalRequest.toString());
        PayCodeAuthorizeResponse reversal = paycodeService.reversal(payCodeReversalRequest);
        return getPayCodeAuthorizeResponseResponseEntity(reversal);
    }

    private ResponseEntity<PayCodeAuthorizeResponse> getPayCodeAuthorizeResponseResponseEntity(PayCodeAuthorizeResponse reversal) {
        switch (reversal.getResponseCode()) {
            case "63":
                return new ResponseEntity<>(reversal, HttpStatus.UNAUTHORIZED);
            case "06":
                return new ResponseEntity<>(reversal, HttpStatus.INTERNAL_SERVER_ERROR);
            case "25":
                return new ResponseEntity<>(reversal, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(reversal, HttpStatus.OK);
        }
    }
}
