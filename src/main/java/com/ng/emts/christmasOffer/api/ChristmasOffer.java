package com.ng.emts.christmasOffer.api;


import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.data.ChristmasOfferRequestLog;
import com.ng.emts.christmasOffer.model.data.MagicCodeLog;
import com.ng.emts.christmasOffer.model.data.RequestLog;
import com.ng.emts.christmasOffer.model.data.ValueConfig;
import com.ng.emts.christmasOffer.model.request.IPCCRequestBody;
import com.ng.emts.christmasOffer.model.request.SmsRequest;
import com.ng.emts.christmasOffer.repo.RequestLogRepo;
import com.ng.emts.christmasOffer.service.ChristmasOfferService;
import com.ng.emts.christmasOffer.service.ValueConfigService;
import com.ng.emts.christmasOffer.service.magiccode.MagicCodeService;
import com.ng.emts.christmasOffer.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ChristmasOffer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChristmasOfferService christmasOfferServiceImpl;
    private final MagicCodeService magicCodeService;
    private final ValueConfigService valueConfigService;
    private final RequestLogRepo requestLogRepo;

    @Autowired
    public ChristmasOffer(ChristmasOfferService christmasOfferServiceImpl, MagicCodeService magicCodeService, ValueConfigService valueConfigService, RequestLogRepo requestLogRepo) {

        this.christmasOfferServiceImpl = christmasOfferServiceImpl;
        this.magicCodeService = magicCodeService;
        this.valueConfigService = valueConfigService;
        this.requestLogRepo = requestLogRepo;
    }

    @PostMapping(
            value = "/apply",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {"application/json", "application/xml", "application/text"})
    private void doChristmasOffer(@RequestParam Map<String, String> ussdRequestMap) throws Exception {
        IPCCRequestBody requestBody = new IPCCRequestBody();
        String message = ussdRequestMap.get("message");
        requestBody.setRequestString(message);
        String msisdn = ussdRequestMap.get("msisdn");
        requestBody.setMsisdn(msisdn);
        String shortCode = ussdRequestMap.get("shortCode");
        requestBody.setShortCode(shortCode);
        requestBody.setServiceId(ussdRequestMap.get("service"));
        requestBody.setVasId(ussdRequestMap.get("vasid"));
        requestBody.setSubscriptionDate(Config.setSubscriptionDate());

        logger.info("############### INCOMING REQUEST ####################");
        logger.info(requestBody.toString());
        LocalDateTime requestInTime = LocalDateTime.now();
        RequestLog requestLog = new RequestLog(msisdn, message, shortCode, requestInTime, null);
        requestLogRepo.save(requestLog);
        christmasOfferServiceImpl.doNationalGiftCard(requestBody, requestLog);
    }

    @GetMapping("/get-magiccode")
    public ResponseEntity<MagicCodeLog> getMagicCode(@RequestParam String magicCode){
        if(!StringUtils.hasText(magicCode)){
            throw new BadRequestException("Please specify a magic code");
        }
        MagicCodeLog byMagicCode = magicCodeService.getByMagicCode(magicCode);
        return new ResponseEntity<>(byMagicCode, HttpStatus.OK);
    }
    @GetMapping("/get-bymsisdn")
    public ResponseEntity<ChristmasOfferRequestLog> getByMsisdn(@RequestParam String msisdn){
        ChristmasOfferRequestLog byMsisdn = christmasOfferServiceImpl.getByMsisdn(msisdn);
        return new ResponseEntity<>(byMsisdn, HttpStatus.OK);
    }
    @PostMapping("/add-config")
    public ResponseEntity<?> addConfig(@RequestBody ValueConfig config){
        valueConfigService.addConfig(config);
        christmasOfferServiceImpl.setDefault();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/allconfig")
    public ResponseEntity<List<ValueConfig>> allConfig(){
        List<ValueConfig> valueConfigs = valueConfigService.valueConfigs();
        return new ResponseEntity<>(valueConfigs, HttpStatus.OK);
    }
    @GetMapping("/refresh")
    public ResponseEntity<ChristmasOfferRequestLog> refresh(@RequestBody ValueConfig config){
        christmasOfferServiceImpl.setDefault();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/offerlog")
    public ResponseEntity<List<ChristmasOfferRequestLog>> offerlog(){
        List<ChristmasOfferRequestLog> all = christmasOfferServiceImpl.all();
        if(all != null && all.size() >= 1){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest request){
        String sms = christmasOfferServiceImpl.sendSms(request);
        return  new ResponseEntity<>(sms, HttpStatus.CREATED);
    }
}
