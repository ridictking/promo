package com.ng.emts.christmasOffer.service;


import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.exceptions.NotFoundException;
import com.ng.emts.christmasOffer.model.data.*;
import com.ng.emts.christmasOffer.model.request.IPCCRequestBody;
import com.ng.emts.christmasOffer.model.request.MainQuerySubClassRequest;
import com.ng.emts.christmasOffer.model.request.PaycodeRequest;
import com.ng.emts.christmasOffer.model.request.SmsRequest;
import com.ng.emts.christmasOffer.model.request.besrequest.*;
import com.ng.emts.christmasOffer.model.request.cbsrequest.changeappendant.ChangeAppendantProductResponseData;
import com.ng.emts.christmasOffer.model.request.cbsrequest.changeappendant.ChangeAppendantRequestData;
import com.ng.emts.christmasOffer.model.response.PaycodeResponse;
import com.ng.emts.christmasOffer.model.response.besresponse.CreateSupplementaryOfferingModificationOrderResponseData;
import com.ng.emts.christmasOffer.model.response.querysubscriber.MainQuerySubClass;
import com.ng.emts.christmasOffer.model.response.querysubscriber.Subscriber;
import com.ng.emts.christmasOffer.repo.ChristmasOfferRepo;
import com.ng.emts.christmasOffer.repo.MsisdnCodeRepo;
import com.ng.emts.christmasOffer.repo.RequestLogRepo;
import com.ng.emts.christmasOffer.service.isw.PaycodeService;
import com.ng.emts.christmasOffer.service.magiccode.MagicCodeServiceImpl;
import com.ng.emts.christmasOffer.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChristmasOfferServiceImpl implements ChristmasOfferService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CbsBesClientService cbsBesClientService;
    private final MagicCodeServiceImpl magicCodeService;
    private final ChristmasOfferRepo repo;
    private final SmsClient smsClient;
    private final QuerySubClient querySubClient;
    private final MsisdnCodeRepo msisdnRepo;
    private final RequestLogRepo requestLogRepo;
    private String OFFERING_CODE_A;
    private String OFFERING_CODE_B;
    private String SERVICE_ID ;
    private String SMS_A = "Congratulations! You have been gifted Free data. 30MB and 60MB locked data valid till %s  Buy data worth of N500 to unlock 30MB or N1000 to unlock 60MB within 2 days" ;
    private String SMS_B ="Congratulations! You have been gifted Free data. 30MB and 60MB locked data valid till %s" +
            " Buy data worth of N500 to unlock 30MB or N1000 to unlock 60MB within 2 days. " +
            "Plus an extra 70MB locked data for x years/months to be credited to you every month from next month";
    private String SMS_C1 ;
    private String SMS_C2 = "Your Pay-Code is %s and your PIN is %s,’ please visit an ATM with cashless withdrawal within %s days and use %s to redeem your cash prize." ;
    private int VALIDITY_PERIOD;
    private LocalDate LAUNCH_DATE;
    private LocalDate END_DATE;
    private final ValueConfigService valueConfigService;
    private final PaycodeService paycodeService;
    private Map<String,String> configs = new HashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
    private Map<String,Integer> categoryMap = new HashMap<>();
    private List<String> categories = Arrays.asList("A","B","C");

    @Autowired
    public ChristmasOfferServiceImpl(CbsBesClientService cbsBesClientService, MagicCodeServiceImpl magicCodeService, ChristmasOfferRepo repo, Config config, SmsClient smsClient, QuerySubClient querySubClient, MsisdnCodeRepo msisdnRepo, RequestLogRepo requestLogRepo, ValueConfigService valueConfigService, PaycodeService paycodeService) {
        this.cbsBesClientService = cbsBesClientService;
        this.magicCodeService = magicCodeService;
        this.repo = repo;
        this.smsClient = smsClient;
        this.querySubClient = querySubClient;
        this.msisdnRepo = msisdnRepo;
        this.requestLogRepo = requestLogRepo;
        this.valueConfigService = valueConfigService;
        this.paycodeService = paycodeService;
        setDefault();
        //setCategories();
        //loadData();
//        generateCat("A",0,20);
//        generateCat("B",0,20);
//        generateCat("C",3000,10);
//        generateCat("C",5000,10);
//        generateCat("C",7000,10);
//        generateCat("C",10000,10);
    }

    public void setCategories(){
        for(String cat : categories){
            categoryMap.put(cat, 0);
        }
    }

    public void setDefault() {
        List<ValueConfig> valueConfigs = valueConfigService.valueConfigs();
        configs = new HashMap<>();
        for (ValueConfig vc : valueConfigs) {
            configs.put(vc.getConfigName(),vc.getConfigValue());
        }
        SERVICE_ID =  configs.get("SERVICE_ID");
        OFFERING_CODE_A =  configs.get("OFFERING_CODE_A");
        OFFERING_CODE_B =  configs.get("OFFERING_CODE_B");
        SMS_A =  configs.get("SMS_A");
        String B1 =  configs.get("SMS_B1");
        String B2 =  configs.get("SMS_B2");
        SMS_B = B1 + B2;
        LAUNCH_DATE = LocalDate.parse(configs.get("LAUNCH_DATE"), formatter);
        END_DATE = LocalDate.parse(configs.get("END_DATE"), formatter);
        VALIDITY_PERIOD = Integer.parseInt(configs.get("VALIDITY_PERIOD"));
        logger.info("SERVICE_ID : "+ SERVICE_ID);
        logger.info("OFFERING_CODE_A : "+ OFFERING_CODE_A);
        logger.info("OFFERING_CODE_B : "+ OFFERING_CODE_B);
        logger.info("LAUNCH_DATE : "+ LAUNCH_DATE);
        logger.info("END_DATE : "+ END_DATE);
        logger.info("VALIDITY_PERIOD : "+ VALIDITY_PERIOD);
        logger.info("SMS_C1 : "+ SMS_C1);
        logger.info("SMS_C2 : "+ SMS_C2);
        logger.info("SMS_B : "+ SMS_B);
    }


    @Override
    public String sendSms(SmsRequest request){
        if(StringUtils.hasText(request.getMsisdn())){
            return smsClient.sendSms1(request).getBody();
        }else {
            throw new BadRequestException("Recipient Msisdn cannot be absent");
        }
    }

    @Override
    public ChristmasOfferRequestLog getByMsisdn(String msisdn){
        if(!StringUtils.hasText(msisdn)){
            throw new BadRequestException("Msisdn cannot be empty");
        }
        ChristmasOfferRequestLog byMsisdn = repo.findByMsisdn(msisdn);
        if(byMsisdn == null){
            throw new NotFoundException("No Record found");
        }
        MagicCodeLog byMagicCode = magicCodeService.getByMagicCode(byMsisdn.getMagicCode());
        byMsisdn.setMagicCodeCategory(byMagicCode.getCategory());
        return byMsisdn;
    }

    @Override
    public List<ChristmasOfferRequestLog> all() {
        return repo.findAll();
    }

    private SmsRequest buildSmsRequest(String msisdn, String message){
        SmsRequest request = new SmsRequest();
        request.setCorrelationId(Config.generateUUID());
        request.setMsgType("0");
        request.setServiceid(SERVICE_ID);
        request.setMsisdn(Config.stripMsisdn(msisdn));
        request.setShortCode(2692);
        request.setMsg(message);
        return request;
    }

    //Do christmas offer
    @Override
    public void doNationalGiftCard(IPCCRequestBody request, RequestLog requestLog) {
        if(LocalDate.now().equals(END_DATE)){
            sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),"Sorry, the National gift card promo has ended."));
            return;
        }
        String magicCode = Config.getMagicCode(request.getRequestString());
        MagicCodeLog byMagicCode = magicCodeService.getByMagicCode(magicCode);
        checkMagicCodeExist(request, magicCode, byMagicCode, requestLog);
        checkMagicCodeUsed(request, magicCode, byMagicCode,requestLog);
        ChristmasOfferRequestLog byMsisdn = repo.findByMsisdn(request.getMsisdn());
        checkMsisdnClaimStatus(request, byMsisdn,requestLog);
        LocalDate expiry = LocalDate.now().plusDays(VALIDITY_PERIOD);
        MsisdnLog whiteListedMsisdn = msisdnRepo.findByMsisdn(Config.stripMsisdn(request.getMsisdn()));
        if(whiteListedMsisdn ==null){
            //send sms
            sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),"Dear customer, you are not eligible for the promo. Please call 200 for more info"));
            updateRequestLog(requestLog);
            throw new BadRequestException("Sorry you are not eligible for this promo");
        }
        doCategoryA(request, byMagicCode, expiry, requestLog);
        doCategoryB(request, byMagicCode, expiry, requestLog);
        doCategoryC(request, byMagicCode, expiry, requestLog);
    }

    private void logMagicCode(MagicCodeLog byMagicCode, ChristmasOfferRequestLog log) {
        if (null != log) {
            byMagicCode.setDateUsed(log.getTransactionDate());
            byMagicCode.setMsisdn(log.getMsisdn());
            byMagicCode.setUsed(true);
            magicCodeService.addMagicCode(byMagicCode);
        }
    }


    private ChristmasOfferRequestLog logChristmasOfferRequest(IPCCRequestBody request, String magicCode, MagicCodeLog byMagicCode,List<String> transactionIds) {
        ChristmasOfferRequestLog log = null;
        if(!transactionIds.isEmpty()){
            for(String transactionId : transactionIds){
                log  = new ChristmasOfferRequestLog(transactionId,
                        request.getMsisdn(),
                        true,
                        magicCode,
                        LocalDateTime.now(), byMagicCode.getCategory());
                log.setCbsTrxnStatus(true);
                repo.save(log);
            }
        }else{
            log = new ChristmasOfferRequestLog(null,request.getMsisdn(),false,magicCode,LocalDateTime.now(),"");
            log.setCbsTrxnStatus(true);
            repo.save(log);
        }
        return log;
    }

private void doCategoryA(IPCCRequestBody request, MagicCodeLog byMagicCode, LocalDate expiry, RequestLog requestLog) {
    if(byMagicCode.getCategory().equals("A")){
        //build data
        List<String> transactionIds = new ArrayList<>();
        ChangeAppendantRequestData requestData = buildChangeAppendantOperation(request, OFFERING_CODE_A);
        logger.info("CBS request data " + requestData);
        ChangeAppendantProductResponseData cbsResponse_A = callCBS(requestData);
        if(!cbsResponse_A.getDescription().equalsIgnoreCase("Operation successful!")) {
            failedCBSCall(request, cbsResponse_A, requestLog);
        }
        transactionIds.add(requestData.getEffectiveDate());
        sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()), String.format(SMS_A, expiry)));
        ChristmasOfferRequestLog christmasOfferRequestLog = logChristmasOfferRequest(request, byMagicCode.getMagicCode(), byMagicCode,transactionIds);
        logMagicCode(byMagicCode,christmasOfferRequestLog);
        updateRequestLog(requestLog);
        callBES(request,OFFERING_CODE_A);
    }
}

private void doCategoryB(IPCCRequestBody request, MagicCodeLog byMagicCode, LocalDate expiry, RequestLog requestLog) {
    if(byMagicCode.getCategory().equals("B")){
        List<String> transactionIds = new ArrayList<>();
        //check activation date for msisdn
        ChangeAppendantRequestData requestData_A = buildChangeAppendantOperation(request, OFFERING_CODE_A);
        logger.info("CBS request data " + requestData_A);
        ChangeAppendantProductResponseData cbsResponse_A = callCBS(requestData_A);
        if(!cbsResponse_A.getDescription().equalsIgnoreCase("Operation successful!")){
            failedCBSCall(request, cbsResponse_A,requestLog);
        }
        transactionIds.add(requestData_A.getEffectiveDate());
        ChangeAppendantRequestData requestData_B = buildChangeAppendantOperation(request, OFFERING_CODE_B);
        ChangeAppendantProductResponseData cbsResponse_B = callCBS(requestData_B);
        if(!cbsResponse_B.getDescription().equalsIgnoreCase("Operation successful!")){
            failedCBSCall(request, cbsResponse_B,requestLog);
        }
        transactionIds.add(requestData_A.getEffectiveDate());
        sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()), String.format(SMS_B, expiry)));
        ChristmasOfferRequestLog christmasOfferRequestLog = logChristmasOfferRequest(request, byMagicCode.getMagicCode(), byMagicCode,transactionIds);
        logMagicCode(byMagicCode,christmasOfferRequestLog);
        updateRequestLog(requestLog);
        callBES(request,OFFERING_CODE_A);
        callBES(request,OFFERING_CODE_B);
    }
}
    private void doCategoryC(IPCCRequestBody request, MagicCodeLog byMagicCode, LocalDate expiry, RequestLog requestLog) {
        if(byMagicCode.getCategory().equals("C")){
            //check activation date for msisdn
            List<String> transactionIds = new ArrayList<>();
            ChangeAppendantRequestData requestData_A = buildChangeAppendantOperation(request, OFFERING_CODE_A);
            logger.info("CBS request data " + requestData_A);
            ChangeAppendantProductResponseData cbsResponse_A = callCBS(requestData_A);
            if(!cbsResponse_A.getDescription().equalsIgnoreCase("Operation successful!")){
                failedCBSCall(request, cbsResponse_A, requestLog);
            }
            transactionIds.add(requestData_A.getEffectiveDate());
            sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()), String.format(SMS_A, expiry)));
            ChristmasOfferRequestLog christmasOfferRequestLog = logChristmasOfferRequest(request, byMagicCode.getMagicCode(), byMagicCode,transactionIds);
            logMagicCode(byMagicCode,christmasOfferRequestLog);
            String pin = Config.generatePin();
            PaycodeResponse payCode = paycodeService.getPayCode(buildPaycodeRequest(byMagicCode,request.getMsisdn(),pin));
            int time = 0;
            if (payCode != null) {
                String tokenLifeTimeInMinutes = payCode.getTokenLifeTimeInMinutes();
                time = Integer.parseInt(tokenLifeTimeInMinutes);
            }
            int daysToExpiry = time /(24*60);
            String payWithMobileToken = payCode.getPayWithMobileToken();
            sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()), String.format(SMS_C2, payWithMobileToken,pin, daysToExpiry, payWithMobileToken)));
            updateRequestLog(requestLog);
            paycodeService.savePaycode(buildPayCodeLogData(payCode,pin));
            callBES(request,OFFERING_CODE_A);
        }
    }

    private void updateRequestLog(RequestLog requestLog){
        RequestLog dbRequestLog = requestLogRepo.findByRequestInTimeAndMsisdn(requestLog.getRequestInTime(), requestLog.getMsisdn());
        dbRequestLog.setRequestOutTime(LocalDateTime.now());
        requestLogRepo.save(dbRequestLog);
    }

    private PayCodeLog buildPayCodeLogData(PaycodeResponse payCode, String pin) {
        return new PayCodeLog(payCode.getPayWithMobileToken(),
                pin,
                "234"+Config.stripMsisdn(payCode.getSubscriberId()),
                LocalDateTime.now(),
                payCode.getTokenLifeTimeInMinutes());
    }

    private PaycodeRequest buildPaycodeRequest(MagicCodeLog request,String msisdn, String pin) {
        PaycodeRequest paycodeRequest = new PaycodeRequest();
        String ttid = Config.transactionId();
        paycodeRequest.setTtid(ttid);
        paycodeRequest.setTransactionRef(ttid);
        paycodeRequest.setTokenLifeTimeInMinutes(String.valueOf(1440));
        //paycodeRequest.setOneTimePin("4322");
        paycodeRequest.setOneTimePin(pin);
        paycodeRequest.setAmount(String.valueOf(request.getCatCPrice()*100));
        paycodeRequest.setMsisdn("234"+Config.stripMsisdn(msisdn));
        paycodeRequest.setPaymentMethodCode("WEMA");
        paycodeRequest.setPaymentMethodTypeCode("MMO");
        paycodeRequest.setPayWithMobileChannel("ATM");
        paycodeRequest.setCodeGenerationChannel("USSD");
        paycodeRequest.setCodeGenerationChannelProvider("NMO");
        paycodeRequest.setFrontEndPartnerId("NMO");
        return paycodeRequest;
    }

    private void failedCBSCall(IPCCRequestBody request, ChangeAppendantProductResponseData cbsResponse_A, RequestLog requestLog) {
        String smsResponse;
        //smsResponse = sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()), "Sorry, your package is not eligible for the National Gift card promo" ));
        updateRequestLog(requestLog);
        ChristmasOfferRequestLog christmasOfferRequestLog = logChristmasOfferRequest(request, "", null,new ArrayList<>());
        throw new BadRequestException(cbsResponse_A.getDescription());
    }

    private ChangeAppendantProductResponseData callCBS(ChangeAppendantRequestData requestData_A) {
        ChangeAppendantProductResponseData cbsResponse_A = cbsBesClientService.changeAppendantProductResponseData(requestData_A);
        logger.info("Response from CBS " + cbsResponse_A.toString());
        return cbsResponse_A;
    }

    private void callBES(IPCCRequestBody request, String offeringCode) {
        CreateSupplementaryOfferingModificationOrderResponseData besResponse = cbsBesClientService.
                createSupplementaryOfferingModificationOrderv2(buildBesData(request, offeringCode));
        logger.info("Response from BES " + besResponse.toString());
    }

    private void checkMsisdnClaimStatus(IPCCRequestBody request, ChristmasOfferRequestLog byMsisdn, RequestLog requestLog) {
        if(byMsisdn != null && StringUtils.hasText(byMsisdn.getMagicCode())){
            //send sms
            String claimedMsisdnMessage = "Sorry, you have already claimed a magic code in the National gift card offer. " +
                    "You can claim only one magic code";
            logger.info("####################  Sending message " + claimedMsisdnMessage + " to "+ request.getMsisdn()+ " ############################");
            String s = sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),
                    claimedMsisdnMessage));
            updateRequestLog(requestLog);
            logger.info("####################  Response from SMS service " + s + " ############################");

            throw new BadRequestException("Msisdn already redeemed magic code " + byMsisdn.getMagicCode());
        }
    }

    private void checkMagicCodeUsed(IPCCRequestBody request, String magicCode, MagicCodeLog byMagicCode, RequestLog requestLog) {
        if(byMagicCode.isUsed()){
            //send sms
            String usedMagicCodeMessage = "Sorry, this National gift card magic code has already been used.";
            logger.info("####################  Sending message " + usedMagicCodeMessage + " to "+ request.getMsisdn()+ " ############################");
            String s = sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),
                    usedMagicCodeMessage));
            updateRequestLog(requestLog);
            logger.info("####################  Response from SMS service " + s + " ############################");
            throw new BadRequestException("Magic Code, " + magicCode + " is already used");
        }
    }

    private void checkMagicCodeExist(IPCCRequestBody request, String magicCode, MagicCodeLog byMagicCode, RequestLog requestLog) {
        if(byMagicCode == null){
            //send sms
            String invalidMagicCodeMessage = "Sorry, the magic code used is invalid. Please recheck and retry";
            String s = sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),
                    invalidMagicCodeMessage));
            logger.info("####################  Sending message " + invalidMagicCodeMessage + " to "+ request.getMsisdn()+ " ############################");
            logger.info("####################  Response from SMS service " + s + " ############################");
            updateRequestLog(requestLog);
            throw new NotFoundException("Magic Code, " + magicCode + " does not exist");
        }
    }

    private LocalDate getActivationDate(IPCCRequestBody request) {
        String activeDate = null;
        ResponseEntity<MainQuerySubClass> subClassResponseEntity =
                querySubClient.query(buildQuerySubObject(request));
        Subscriber subscriber = subClassResponseEntity.getBody().getSubscriber();
        activeDate = subscriber.getSubscriber().getSubscriberInformation().getActiveDate();
        if(!StringUtils.hasText(activeDate)){
            activeDate = subscriber.getSubscriber().getSubscriberInformation().getEffectiveDate();
        }
        return Config.getActiveDate(activeDate);
    }

    private void checkActivationStatus(IPCCRequestBody request, LocalDate activationDate) {
        if(activationDate.isBefore(LAUNCH_DATE)){
            String s = sendSms(buildSmsRequest(Config.stripMsisdn(request.getMsisdn()),
            "This promo is only available to newly activated lines. Please call 200 for more info."));
            throw new BadRequestException("This promo is only available to newly activated lines. " +
                    "Please call 200 for more info. "+ request.getMsisdn());
        }
    }

    private MainQuerySubClassRequest buildQuerySubObject(IPCCRequestBody request) {
        return new MainQuerySubClassRequest(Config.stripMsisdn(request.getMsisdn()),"NATIONAL_GIFT_PROMO");
    }

    public CreateSupplementaryOfferingModificationOrderRequestData buildBesData(IPCCRequestBody request, String offeringCode) {
        CreateSupplementaryOfferingModificationOrderRequestData csomoRequestData = new CreateSupplementaryOfferingModificationOrderRequestData();
        csomoRequestData.setReason("createSupplementaryOfferingModificationOrder");
        csomoRequestData.setAppName("CHRISTMAS-OFFER");

        OrderData orderData = new OrderData();
        BusinessOwnershipData businessOwnership = new BusinessOwnershipData();
        businessOwnership.setBeId("101");
        businessOwnership.setBeCode(null);
        orderData.setBusiOwnership(businessOwnership);
        orderData.setBusinessFee(null);
        orderData.setContactPersonInfo(null);
        orderData.setInvoiceInfo(null);
        OrderBasicInfoData orderBasicInfo = new OrderBasicInfoData();
        orderData.setOrderBasicInfo(orderBasicInfo);
        orderData.setPaymentInfo(null);

        ArrayList<OrderItemData> orderItemArrayData = new ArrayList<>();
        OrderItemData theOrderItemData = new OrderItemData();
        theOrderItemData.setBusinessAction("2");
        theOrderItemData.setProperty(null);
        theOrderItemData.setId(null);
        ApplyObjectData applyObject = new ApplyObjectData("S", null, Config.stripMsisdn(request.getMsisdn()));
        theOrderItemData.setApplyObject(applyObject);
        ArrayList<OfferingData> offeringDataArray = new ArrayList<>();
        OfferingData theOfferingData = new OfferingData(String.valueOf(offeringCode), null, "A");
        theOfferingData.setAction("A");

        ArrayList<OfferingCharacteristicsData> offeringxtrcsArray = new ArrayList<>();

        theOfferingData.setOfferingCharacteristics(offeringxtrcsArray);

        offeringDataArray.add(theOfferingData);
        theOrderItemData.setOffering(offeringDataArray);
        orderItemArrayData.add(theOrderItemData);
        orderData.setOrderItem(orderItemArrayData);
        csomoRequestData.setOrder(orderData);
        return csomoRequestData;
    }

    public ChangeAppendantRequestData buildChangeAppendantOperation(IPCCRequestBody requestBody, String offeringCode){
        String transId = Long.toString(System.currentTimeMillis()) + Integer.toString(Math.abs(new Random().nextInt(99999)));
        ChangeAppendantRequestData requestData = new ChangeAppendantRequestData();
        requestData.setAppName("CHRISTMAS-PROMO");
        requestData.setOperationType(1);
        requestData.setMsisdn(Config.stripMsisdn(requestBody.getMsisdn()));
        requestData.setProductStatus("0");
        requestData.setProductId(new String[]{offeringCode});
        requestData.setTxnId(transId);
        requestData.setEffectiveDate(Config.cbsTimestampToString(requestBody.getSubscriptionDate()));

        return  requestData;
    }

    private void loadData(){
        String[] msisdns = {
//                "8094194062",
//                "8094192604",
//                "8094191273",
//                "8094191604",
//                "8094191597",
//                "8094192024",
//                "8094191881",
//                "8094190815",
//                "8094190168",
//                "8094190170",
//                "8094190912",
//                "8094191588",
//                "8094191099",
//                "8094192232"
//                "8094192430",
//                "8094192187",
//                "8094196125"
                "8094195328",
                "8094195915",
                "8094199552"

        };
        for (String msisdn : msisdns) {
            msisdnRepo.save(new MsisdnLog(Config.stripMsisdn(msisdn),false));
        }
    }
//    "8094199707",
//            "8094198946",
//            "8094193372",
//            "8094199565",
//            "8094194122",
//            "8094199887",
//            "8094197418",
//            "8094198256",
//            "8094193326",
//            "8094192639",
//            "8094199606"

    private void generateCat(String cat, int price, int limit){
        List<Long> codes = Stream.generate(Math::random)
                .map(g -> Math.round(g * 1000000))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
        System.out.println(codes);
        codes.forEach(x -> magicCodeService.addMagicCode(new MagicCodeLog(String.valueOf(x),cat,false,null,null,price)));

    }
   // @Scheduled(cron = "0 0 0 */3 * *")
    private void clearDb(){
        logger.info("Deleting all elements in the request Log table");
        requestLogRepo.deleteAll();
    }
}
