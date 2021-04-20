package com.ng.emts.christmasOffer.service.isw;

import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.data.*;
import com.ng.emts.christmasOffer.model.request.PaycodeRequest;
import com.ng.emts.christmasOffer.model.response.PayCodeStatusResponse;
import com.ng.emts.christmasOffer.model.response.PaycodeResponse;
import com.ng.emts.christmasOffer.repo.PayCodeAuthorizeRequestRepo;
import com.ng.emts.christmasOffer.repo.PayCodeLogRepo;
import com.ng.emts.christmasOffer.repo.PayCodeReversalRequestRepo;
import com.ng.emts.christmasOffer.service.ValueConfigService;
import com.ng.emts.christmasOffer.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaycodeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PaycodeClient paycodeClient;
    private int THREE_K_COUNT = 0;
    private int FIVE_K_COUNT = 0;
    private int SEVEN_K_COUNT = 0;
    private int TEN_K_COUNT = 0;
    private LocalDate startDate = LocalDate.of(2021, Month.JANUARY,1);
    private final ValueConfigService valueConfigService;
    private Map<String,String> configs = new HashMap<>();
    private Map<String,Integer> amountMap = new HashMap<>();
    private List<Integer> amounts = Arrays.asList(3000, 5000, 7000, 10000);
    private final PayCodeLogRepo payCodeLogRepo;
    private final PayCodeAuthorizeRequestRepo payCodeAuthorizeRequestRepo;
    private final PayCodeReversalRequestRepo payCodeReversalRequestRepo;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");




    @Autowired
    public PaycodeService(PaycodeClient paycodeClient, ValueConfigService valueConfigService, PayCodeLogRepo payCodeLogRepo, PayCodeAuthorizeRequestRepo payCodeAuthorizeRequestRepo, PayCodeReversalRequestRepo payCodeReversalRequestRepo) {
        this.paycodeClient = paycodeClient;
        this.valueConfigService = valueConfigService;
        this.payCodeLogRepo = payCodeLogRepo;
        this.payCodeAuthorizeRequestRepo = payCodeAuthorizeRequestRepo;
        this.payCodeReversalRequestRepo = payCodeReversalRequestRepo;
        setDefault();
    }

    public void setDefault() {
        List<ValueConfig> valueConfigs = valueConfigService.valueConfigs();
        configs = new HashMap<>();
        for (ValueConfig vc : valueConfigs) {
            configs.put(vc.getConfigName(),vc.getConfigValue());
        }
        startDate = LocalDate.parse(configs.get("LAUNCH_DATE"), formatter);
        logger.info("LAUNCH_DATE : "+ startDate);
        for(Integer amount : amounts){
            amountMap.put(String.valueOf(amount), 0);
        }
    }

    private long getAmount(){
        //Optional<Integer> anyAmount = amounts.stream().findAny();
        updateStatus();
        Optional<String> anyAmount = amountMap.entrySet().stream().filter(x -> x.getValue() == 0).map(Map.Entry::getKey).findAny();
        int amount = Integer.parseInt(anyAmount.get());
        if(amount == 3000){
            THREE_K_COUNT++;
            return amount;
        }
        if(amount == 5000){
            FIVE_K_COUNT++;
            return amount;
        }
        if(amount == 7000) {
            SEVEN_K_COUNT++;
            return amount;
        }
        if(amount == 10000) {
            TEN_K_COUNT++;
            return amount;
        }
        return 0L;
    }

    private void updateStatus() {
        if(THREE_K_COUNT >= 6173) {
            amountMap.replace("3000",1);
        }if(FIVE_K_COUNT >= 200) {
            amountMap.replace("5000",1);
        }
        if(SEVEN_K_COUNT >= 40) {
            amountMap.replace("7000",1);
        }
        if(TEN_K_COUNT < 20) {
            amountMap.replace("10000",1);
        }
    }

    public PaycodeResponse getPayCode(PaycodeRequest request) {
        return paycodeClient.generatePaycode(request);
    }

    public PayCodeStatusResponse getStatus(PaycodeRequest request){
        return paycodeClient.getStatus(request);
    }
    public void savePaycode(PayCodeLog payCodeLog){
        if(payCodeLog != null){
            payCodeLogRepo.save(payCodeLog);
        }
    }

    public PayCodeAuthorizeResponse reversal(PayCodeReversalRequest paycode) {
        PayCodeAuthorizeResponse payCodeAndCheck;
        boolean checkForNull = Config.checkForNull(paycode.getSubscriberId(), paycode.getPaycode(), paycode.getMacdata());
        if(!checkForNull) {
             payCodeAndCheck = new PayCodeAuthorizeResponse("06", Interswitch.SOMETHING_HAPPENED);
            return payCodeAndCheck;
        }
        String macData = generateReversalMacData(paycode.getSubscriberId(),
                paycode.getPaycode());
        if(!macData.equals(paycode.getMacdata())){
            payCodeAndCheck =  new PayCodeAuthorizeResponse("63", Interswitch.SECURITY_VIOLATION);
            return payCodeAndCheck;
        }
        payCodeAndCheck = getPayCodeAndCheck(paycode.getPaycode(), paycode.getSubscriberId());
        paycode.setStatus(payCodeAndCheck.getResponseDescription());
        payCodeReversalRequestRepo.save(paycode);
        return payCodeAndCheck;
    }

    private PayCodeAuthorizeResponse getPayCodeAndCheck(String paycode2, String subscriberId) {
        PayCodeLog payCodeLog = this.checkAndGetPayCode(paycode2);
        if (payCodeLog != null && payCodeLog.getMsisdn().equals(subscriberId) ) {
            return new PayCodeAuthorizeResponse("00", Interswitch.AUTHORIZE);
        } else {
            return new PayCodeAuthorizeResponse("25", Interswitch.NOT_FOUND);
        }
    }

    public PayCodeAuthorizeResponse authorize(PaycodeAuthorizeRequest paycode){
        boolean checkForNull = Config.checkForNull(paycode.getSubscriberId(), paycode.getProviderToken(), paycode.getTransactionType(), paycode.getPaycode(), paycode.getMacdata());
        if(!checkForNull) {
            return new PayCodeAuthorizeResponse("06", Interswitch.SOMETHING_HAPPENED);
            //throw new BadRequestException()
        }
        String macData = generateAuthorizeMacData(paycode.getSubscriberId(),
                paycode.getProviderToken(),
                paycode.getTransactionType(),
                paycode.getPaycode());
        if(!macData.equals(paycode.getMacdata())){
            return new PayCodeAuthorizeResponse("63", Interswitch.SECURITY_VIOLATION);
        }
        PayCodeAuthorizeResponse payCodeAndCheck = getPayCodeAndCheck(paycode.getPaycode(), paycode.getSubscriberId());
        paycode.setStatus(payCodeAndCheck.getResponseDescription());
        payCodeAuthorizeRequestRepo.save(paycode);
        return payCodeAndCheck;
    }


    public PayCodeLog checkAndGetPayCode(String paycode){
        if (StringUtils.hasText(paycode))
            return payCodeLogRepo.findByPayCode(paycode);
        return null;
    }

    private static String generateAuthorizeMacData(String subscriberId,
                                                   String providerToken,
                                                   String transactionType,
                                                   String paycode){
        String delimiter = "&";
        String signatureCipher = subscriberId +
                delimiter +
                providerToken +
                delimiter +
                transactionType +
                delimiter +
                paycode +
                delimiter +
                Interswitch.SHARED_SECRET_KEY;

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest
                    .getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
        byte[] signatureBytes = messageDigest
                .digest(signatureCipher.getBytes());
        // encode signature as base 64

        String cipher = new String(Base64.getEncoder().encode(signatureBytes));
        return cipher;
    }

    private static String generateReversalMacData(String subscriberId,
                                                  String paycode){
        String delimiter = "&";
        String signatureCipher = subscriberId +
                delimiter +
                paycode +
                delimiter +
                Interswitch.SHARED_SECRET_KEY;

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest
                    .getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
        byte[] signatureBytes = messageDigest
                .digest(signatureCipher.getBytes());
        // encode signature as base 64

        String cipher = new String(Base64.getEncoder().encode(signatureBytes));
        return cipher;
    }
}


