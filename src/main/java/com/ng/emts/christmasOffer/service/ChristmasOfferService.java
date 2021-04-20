package com.ng.emts.christmasOffer.service;

import com.ng.emts.christmasOffer.model.data.ChristmasOfferRequestLog;
import com.ng.emts.christmasOffer.model.data.RequestLog;
import com.ng.emts.christmasOffer.model.request.IPCCRequestBody;
import com.ng.emts.christmasOffer.model.request.SmsRequest;

import java.util.List;

public interface ChristmasOfferService {
    ChristmasOfferRequestLog getByMsisdn(String msisdn);
    List<ChristmasOfferRequestLog> all();

    //Do christmas offer
    void doNationalGiftCard(IPCCRequestBody request, RequestLog requestLog) throws Exception;
    void setDefault();
    String sendSms(SmsRequest request);
}
