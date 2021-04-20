package com.ng.emts.christmasOffer.service.isw;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 *
 * @author Sheriff.Adebisi
 */
@Component
public class Passport {
    public static final String PASSPORT_RESOURCE_URL = "passport/oauth/token";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";

    public static final String ACCESS_TOKEN = "access_token";

    public static HashMap<String,String> getClientAccessToken(String clientId, String clientSecret, String passportBaseUrl){
    	
    	HashMap<String,String> responseMap = new HashMap<>();
    	
    	String passportUrl = passportBaseUrl + PASSPORT_RESOURCE_URL;
    	String accesToken = null;

        StringBuffer response = null;
        int responseCode = 0;
        try {
            URL obj = new URL(passportUrl);

            System.setProperty("http.maxRedirects", "100");
            java.net.CookieManager cm = new java.net.CookieManager();
            java.net.CookieHandler.setDefault(cm);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            String basicAuthCipher = clientId + ":" + clientSecret;
            String basicAuth = new String(Base64.encode(basicAuthCipher.getBytes()));

            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Basic " + basicAuth);
            String request = "grant_type=client_credentials";

            con.setDoOutput(true);
            con.setConnectTimeout(0);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(request);
            wr.flush();
            wr.close();

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (Exception ex) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseCode = con.getResponseCode();
            if (responseCode == 200) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = new HashMap<String, String>();
                map = mapper.readValue(response.toString(),
                        new TypeReference<Map<String, String>>() {
                });

                accesToken = map.get("access_token");
                System.out.println("This is my access token " + accesToken);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseMap.put(RESPONSE_CODE, String.valueOf(responseCode));
		responseMap.put(RESPONSE_MESSAGE, response.toString());
		responseMap.put(ACCESS_TOKEN, accesToken);

        return responseMap;
    }
}
