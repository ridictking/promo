package com.ng.emts.christmasOffer.service.isw;

import com.google.gson.Gson;
import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.request.PaycodeRequest;
import com.ng.emts.christmasOffer.model.response.PayCodeStatusResponse;
import com.ng.emts.christmasOffer.model.response.PaycodeResponse;
import com.ng.emts.christmasOffer.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaycodeClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;
    private final Config config;
    private final String iswPaycodeBaseUrl = "Http://20.73.220.165:8085/paycode/";
    protected HttpHeaders headers;
    public static final String SANDBOX_PASSPORT_BASE_URL ="https://sandbox.interswitchng.com/";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String NONCE = "NONCE";
    private static final String SIGNATURE_METHOD = "SIGNATURE_METHOD";
    private static final String SIGNATURE = "SIGNATURE";
    private static final String AUTHORIZATION = "AUTHORIZATION";

    @Autowired
    public PaycodeClient(RestTemplate restTemplate, Config config) {
        this.restTemplate = restTemplate;
        this.config = config;
        setDefaultHeader();
    }

    private void setDefaultHeader(){

        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/json");
        this.headers.add("Accept", "application/json");
        //this.headers.add("frontEndPartnerId","WEMA");
    }

    public PaycodeResponse generatePaycode(PaycodeRequest request) {
        String resourceUrl = iswPaycodeBaseUrl + "get";
        HttpEntity<PaycodeRequest> entity =new HttpEntity<>(request,headers);
        PaycodeResponse response = null;
        try {
            logger.info(request.toString());
            ResponseEntity<String> exchange = restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, String.class);
            Gson gson   = new Gson();
            logger.info(exchange.getBody());
            if(exchange.getStatusCode().is2xxSuccessful()){
                response = gson.fromJson(exchange.getBody(), PaycodeResponse.class);
                logger.info(response.toString());
            }else {
                throw new BadRequestException(exchange.getBody());
            }
            return response;
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
    public PayCodeStatusResponse getStatus(PaycodeRequest request) {
        String resourceUrl = iswPaycodeBaseUrl + "status";
        HttpEntity<PaycodeRequest> entity =new HttpEntity<>(request,headers);
        PayCodeStatusResponse response = null;
        try {
            logger.info(request.toString());
            ResponseEntity<String> exchange = restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, String.class);
            Gson gson   = new Gson();
            logger.info(exchange.getBody());
            if(exchange.getStatusCode().is2xxSuccessful()){
                response = gson.fromJson(exchange.getBody(), PayCodeStatusResponse.class);
                logger.info(response.toString());
            }else {
                throw new BadRequestException(exchange.getBody());
            }
            return response;
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
}


