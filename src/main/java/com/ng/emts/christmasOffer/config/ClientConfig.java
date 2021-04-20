package com.ng.emts.christmasOffer.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {
//    @Bean
//    public RestTemplate christMassResTemplate(){
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.179.5.59", 139));
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setProxy(proxy);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        restTemplate.setRequestFactory(requestFactory);
//        requestFactory.setReadTimeout(2000);
//        requestFactory.setConnectTimeout(2000);
//        return restTemplate;
//    }

    @Bean
    public RestTemplate christMassResTemplate(RestTemplateBuilder restTemplateBuilder){
       return restTemplateBuilder.build();
    }
}
