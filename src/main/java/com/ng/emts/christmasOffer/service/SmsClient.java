package com.ng.emts.christmasOffer.service;


import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.request.SmsRequest;
import com.ng.emts.christmasOffer.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SmsClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;
    private final Config config;
    protected HttpHeaders headers;

    @Autowired
    public SmsClient(RestTemplate restTemplate, Config config) {
        this.restTemplate = restTemplate;
        this.config = config;
        setDefaultHeader();
    }

    private void setDefaultHeader(){
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    }


    public ResponseEntity<String> sendSms1(SmsRequest request){
        headers.add("Content-Type", "application/json");
        HttpEntity<SmsRequest> entity =new HttpEntity<>(request,headers);
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(config.getSmsUrl(), HttpMethod.POST, entity, String.class);
            if(exchange.getStatusCode().is2xxSuccessful()){
                logger.info(config.getSmsUrl() + " successfully sent: ****" + request.getMsg()+ "**** to " + request.getMsisdn() );
                return exchange;
            }
        } catch (Exception e) {
            logger.info(".168 is unreachable");
            logger.info(e.getMessage());
            try {
                ResponseEntity<String> exchange2 = restTemplate.exchange(config.getSmsUrl1(), HttpMethod.POST, entity, String.class);
                if(exchange2.getStatusCode().is2xxSuccessful()){
                    logger.info(config.getSmsUrl1() + " successfully sent: ****" + request.getMsg()+ "**** to " + request.getMsisdn() );
                    return exchange2;
                }
            } catch (Exception restClientException) {
                logger.info(".166 is unreachable");
                logger.info(restClientException.getMessage());
                sendSmsUrlEncoded(request);
                restClientException.printStackTrace();
//                logger.error(e.getMessage());
//                throw new BadRequestException(e.getMessage());
            }
        }
        throw new BadRequestException("SMS not sent with the payload "+ request.toString());
    }


    public ResponseEntity<String> sendSmsUrlEncoded(SmsRequest request){
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("msgType",request.getMsgType());
        map.add("msisdn",request.getMsisdn());
        map.add("msg",request.getMsg());
        map.add("shortCode",String.valueOf(request.getShortCode()));
        map.add("serviceid",request.getServiceid());
        map.add("correlationId",String.valueOf(request.getCorrelationId()));
        HttpEntity<MultiValueMap<String, String>> entity =new HttpEntity<>(map,headers);
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(config.getSmsUrlencoded(), HttpMethod.POST, entity, String.class);
            if(exchange.getStatusCode().is2xxSuccessful()){
                logger.info(config.getSmsUrlencoded() + " successfully sent: ****" + request.getMsg()+ "**** to " + request.getMsisdn() );
                return exchange;
            }
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
        throw new BadRequestException("SMS not sent with the payload "+ request.toString());
    }
}
