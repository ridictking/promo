package com.ng.emts.christmasOffer.util;

import com.ng.emts.christmasOffer.exceptions.BadRequestException;
import com.ng.emts.christmasOffer.model.data.IswHeaders;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class Config {
    @Value("${smpp.api.sendSmsUrl}")
    private String smsUrl;
    @Value("${smpp.api.sendSmsUrl1}")
    private String smsUrl1;
    @Value("${smpp.api.sendSmsUrl.urlencoded}")
    private String smsUrlencoded;
    @Value("${bes.api.querysub}")
    private String subQueryUrl;
    @Value("${isw.api.paycodeUrl}")
    private String paycodeUrl;
    @Value("${isw.api.clientId}")
    private String clientId;
    @Value("${isw.api.clientSecret}")
    private String clientSecret;
    @Value("${isw.api.signatureMethod}")
    private String signatureMethod;
    @Value("${isw.api.sandBox.passport.baseurl}")
    private String passportBaseUrl;
    @Value("${isw.api.sandBox.shared.secret}")
    public String sharedSecret;


    public String getSharedSecret() {
        return sharedSecret;
    }

    public String getSmsUrl() {
        return smsUrl;
    }


    public String getSmsUrl1() {
        return smsUrl1;
    }


    public String getSubQueryUrl() {
        return subQueryUrl;
    }


    public String getPaycodeUrl() {
        return paycodeUrl;
    }


    public String getClientId() {
        return clientId;
    }


    public String getClientSecret() {
        return clientSecret;
    }


    public String getSignatureMethod() {
        return signatureMethod;
    }


    public String getPassportBaseUrl() {
        return passportBaseUrl;
    }


    public String getSmsUrlencoded() {
        return smsUrlencoded;
    }

    public void setSmsUrlencoded(String smsUrlencoded) {
        this.smsUrlencoded = smsUrlencoded;
    }

    public static String generateTransactionReference(String paymentVendor, String paymentReference) throws NoSuchAlgorithmException {
        String transactionReference = paymentVendor + "|" + paymentReference + "|" + System.currentTimeMillis();
        return HashUtil.getMD5Hash(transactionReference.getBytes());
    }

    public static String generateTransactionReference(String msisdn){
        String transactionReference = msisdn + "|" + System.currentTimeMillis();
        try {
            return HashUtil.getMD5Hash(transactionReference.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    public static String generateTransactionReference2(String paymentVendor, String paymentReference) throws NoSuchAlgorithmException {
        return paymentVendor + "|" + paymentReference + "|" + System.currentTimeMillis();
    }

    public static UUID generateUUID(){
        return UUID.randomUUID();
    }
    public static LocalDateTime getPresentDate(){
        return LocalDateTime.now();
    }

    public static String stripMsisdn(String msisdn) {
        if (msisdn.startsWith("+234")) {
            msisdn = msisdn.substring(4);
        } else if (msisdn.startsWith("234")) {
            msisdn = msisdn.substring(3);
        } else if (msisdn.startsWith("0")) {
            msisdn = msisdn.substring(1);
        }
        return msisdn;
    }

    public static String getMagicCode(String requestString){
        return requestString.substring(5,requestString.length()-1);
    }

    public static String cbsTimestampToString(Timestamp txDate) {
        Date date = new Date(txDate.getTime());
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(date);
    }
    public static String toDateString(Timestamp txDate) {
        Date date = new Date(txDate.getTime());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    public static Timestamp setSubscriptionDate() {
        Calendar today = Calendar.getInstance();
        return (new Timestamp(today.getTimeInMillis()));
    }
    public static Timestamp addDaysToTimestamp(Timestamp ts, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        cal.add(Calendar.DAY_OF_WEEK, days);
        return new Timestamp(cal.getTime().getTime());
    }
    public static String transactionId(){
        return Integer.toString(Math.abs(new Random().nextInt(999999)));
    }
    public static String generatePin(){
        String pin = Integer.toString(Math.abs(new Random().nextInt(9999)));
        if(pin.length() < 4) pin = pin+ pin.charAt(0);
        return pin;
    }
    public static LocalDate getActiveDate(String dateInString){
        int year = Integer.parseInt(dateInString.substring(0,4));
        int month = Integer.parseInt(dateInString.substring(4,6));
        int day = Integer.parseInt(dateInString.substring(6,8));

        return LocalDate.of(year,month,day);
    }

    public static long payCodeTimestamp(){
        //Timezone MUST be Africa/Lagos.
        TimeZone lagosTimeZone = TimeZone.getTimeZone("Africa/Lagos");
        Calendar calendar = Calendar.getInstance(lagosTimeZone);
        // Timestamp must be in seconds.
        return calendar.getTimeInMillis() / 1000;
    }
    public static String getNonce(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static IswHeaders getHeaders(String httpMethod, String resourceUrl, String clientId,
                                      String clientSecretKey,
                                      String signatureMethod){
        String clientIdBase64 = new String(Base64.encode(clientId.getBytes()));
        String authorization = "InterswitchAuth" + " " + clientIdBase64;

        resourceUrl = resourceUrl.replace("http://", "https://");
        String encodedResourceUrl = null;
        try {
            encodedResourceUrl = URLEncoder.encode(resourceUrl, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long timestamp = payCodeTimestamp();
        String nonce = getNonce();
        String signatureCipher = httpMethod + "&" + encodedResourceUrl + "&"
                + timestamp + "&" + nonce + "&" + clientId + "&"
                + clientSecretKey;
        //System.out.println(signatureCipher);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(signatureMethod);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] signatureBytes = messageDigest.digest(signatureCipher.getBytes());
        // encode signature as base 64

        String signature = new String(Base64.encode(signatureBytes));
        IswHeaders headers = new IswHeaders();
        headers.setNonce(nonce);
        headers.setSignature(signature);
        headers.setTimeStamp(timestamp);
        headers.setSignatureMethod(signatureMethod);
        headers.setAuthorization(authorization);
        return headers;
    }
    public static boolean checkForNull(String... values){
        for(String s : values){
            if(!StringUtils.hasText(s)) return false;
        }
        return true;
    }

}
