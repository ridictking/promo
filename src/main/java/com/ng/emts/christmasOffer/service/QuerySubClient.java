package com.ng.emts.christmasOffer.service;


import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.request.MainQuerySubClassRequest;
import com.ng.emts.christmasOffer.model.response.querysubscriber.MainQuerySubClass;
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

import java.util.Objects;

@Component
public class QuerySubClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;
    private final Config config;
    protected HttpHeaders headers;

    @Autowired
    public QuerySubClient(RestTemplate restTemplate, Config config) {
        this.restTemplate = restTemplate;
        this.config = config;
        setDefaultHeader();
    }

    private void setDefaultHeader(){
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    }

    public ResponseEntity<MainQuerySubClass> query(MainQuerySubClassRequest request){
        headers.add("Content-Type", "application/json");
        HttpEntity<MainQuerySubClassRequest> entity =new HttpEntity<>(request,headers);
        try {
            ResponseEntity<MainQuerySubClass> exchange = restTemplate.exchange(config.getSubQueryUrl(), HttpMethod.POST, entity, MainQuerySubClass.class);
            logger.info(Objects.requireNonNull(exchange.getBody()).toString());
            return exchange;
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }
}
