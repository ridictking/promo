//package com.ng.emts.christmasOffer.model.repos;
//
//
//import com.ng.emts.christmasOffer.model.request.PaycodeClass;
//import com.ng.emts.christmasOffer.service.isw.Passport;
//import com.ng.emts.christmasOffer.util.ConstantUtils;
//import com.ng.emts.christmasOffer.util.Interswitch;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONObject;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//
//@Repository
//public class PaycodeClassInterfaceImpl implements PaycodeClassInterface {
//
//	private static final String TIMESTAMP = "TIMESTAMP";
//	private static final String NONCE = "NONCE";
//	private static final String SIGNATURE_METHOD = "SIGNATURE_METHOD";
//	private static final String SIGNATURE = "SIGNATURE";
//	private static final String AUTHORIZATION = "AUTHORIZATION";
//
//
//
//@SuppressWarnings("deprecation")
//public HttpResponse generatePaycode(PaycodeClass paycode) throws Exception {
//
//		//HashMap<String, String> interswitchResponse;
//
//	String amount = paycode.getAmount();// 100000;
//		String ttid = paycode.getTtid();// "812";
//		String msisdn =  paycode.getMsisdn();//"2348090673520";
//		String payWithMobileChannel = paycode.getPayWithMobileChannel();// "ATM";
//		String tokenLifeTimeInMinutes = paycode.getTokenLifeTimeInMinutes();// "90";
//		String oneTimePin = paycode.getOneTimePin();// "1234";
//		String paymentMethodTypeCode =paycode.getPaymentMethodTypeCode();
//		String paymentMethodCode = paycode.getPaymentMethodCode();
//		String codeGenerationChannel =paycode.getCodeGenerationChannel();
//		String codeGenerationChannelProvider = paycode.getCodeGenerationChannelProvider();
//		String transactionRef = paycode.getTransactionRef();
//		String frontEndPartnerId= paycode.getFrontEndPartnerId();
//
//		//String resourceUrl = ConstantUtils.PWM_BASE_URL +msisdn+"/tokens";
//		String resourceUrl="https://sandbox.interswitchng.com/api/v1/pwm/subscribers/"+msisdn+"/tokens";
//		//String resourceUrl="http://localhost:9080/api/v1/pwm/subscribers/"+msisdn+"/tokens";
//
//		JSONObject json = new JSONObject();
//		json.put("amount", amount);
//		json.put("ttid", ttid);
//		json.put("payWithMobileChannel", payWithMobileChannel);
//		json.put("tokenLifeTimeInMinutes", tokenLifeTimeInMinutes);
//		json.put("oneTimePin", oneTimePin);
//		json.put("paymentMethodTypeCode", paymentMethodTypeCode);
//		json.put("paymentMethodCode", paymentMethodCode);
//		json.put("codeGenerationChannel", codeGenerationChannel);
//		json.put("codeGenerationChannelProvider", codeGenerationChannelProvider);
//		json.put("transactionRef", transactionRef);
//		json.put("frontEndPartnerId", frontEndPartnerId);
//
//		String jsonData = json.toString();
//		String httpMethod="POST";
//		String signatureMethod = "SHA1";
//
//		//interswitchResponse = interswitchPwm.send(resourceUrl, httpMethod, jsonData,extraHeaders);
//		HashMap<String, String> extraHeaders = Interswitch.generateSignature(httpMethod, resourceUrl,paycode.getClientId(), paycode.getClientSecret(),signatureMethod);
//		System.out.println("The Extraheaders is :" + extraHeaders);
//
//				// Write HTTP request to post
//				DefaultHttpClient client = new DefaultHttpClient();
//				//String proxyHost = "172.16.10.20";
//			    //int proxyPort = 8080;
//				  //HttpHost proxy = new HttpHost(proxyHost,proxyPort);
//
//				//client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//
//				HttpPost post = new HttpPost(resourceUrl);
//
//				post.setHeader("Authorization", extraHeaders.get(AUTHORIZATION));
//				post.setHeader("Timestamp", extraHeaders.get(TIMESTAMP));
//				post.setHeader("Nonce", extraHeaders.get(NONCE));
//				post.setHeader("Signature", extraHeaders.get(SIGNATURE));
//				post.setHeader("SignatureMethod", extraHeaders.get(SIGNATURE_METHOD));
//				post.setHeader("frontEndPartnerId",paycode.getFrontEndPartnerId());
//				post.setHeader("ACCESS_TOKEN", Passport.getClientAccessToken(paycode.getClientId(),paycode.getClientSecret(),ConstantUtils.SANDBOX_PASSPORT_BASE_URL).get("access_token"));
//
//
//				StringEntity entity = new StringEntity(jsonData);
//
//				entity.setContentType("application/json");
//				post.setEntity(entity);
//
//				HttpResponse response = client.execute(post);
//		return response;
//	}
//
//public HttpResponse getPaycodeStatus(PaycodeClass paycode) throws Exception {
//String msisdn =  paycode.getMsisdn();//"2348090673520";
//String frontEndPartnerId= paycode.getFrontEndPartnerId();
//String paywithMobile=paycode.getPayCode();
//
//String resourceUrl="https://sandbox.interswitchng.com/api/v1/pwm/info/"+msisdn+"/tokens";
//
//
//String httpMethod="GET";
//String signatureMethod = "SHA1";
//
//
//HashMap<String, String> extraHeaders = Interswitch.generateSignature(httpMethod, resourceUrl,paycode.getClientId(), paycode.getClientSecret(),signatureMethod);
//System.out.println("The Extraheaders is :" + extraHeaders);
//
//		DefaultHttpClient client = new DefaultHttpClient();
////		String proxyHost = "172.16.10.20";
////	    int proxyPort = 8080;
////		  HttpHost proxy = new HttpHost(proxyHost,proxyPort);
////
////		client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
////
//		HttpGet getRequest  = new HttpGet(resourceUrl);
//
//		getRequest.addHeader("Authorization", extraHeaders.get(AUTHORIZATION));
//		getRequest.addHeader("Timestamp", extraHeaders.get(TIMESTAMP));
//		getRequest.addHeader("Nonce", extraHeaders.get(NONCE));
//		getRequest.addHeader("Signature", extraHeaders.get(SIGNATURE));
//		getRequest.addHeader("SignatureMethod", extraHeaders.get(SIGNATURE_METHOD));
//		getRequest.addHeader("frontEndPartnerId",paycode.getFrontEndPartnerId());
//		getRequest.addHeader("ACCESS_TOKEN", Passport.getClientAccessToken(paycode.getClientId(),paycode.getClientSecret(), ConstantUtils.SANDBOX_PASSPORT_BASE_URL).get("access_token"));
//		getRequest.addHeader("Content-Type","application/json");
//		getRequest.addHeader("FrontEndPartnerId",frontEndPartnerId);
//		getRequest.addHeader("paycode",paywithMobile);
//
//	System.out.println("This is the get Request    "+ getRequest);
//
//		HttpResponse response = client.execute(getRequest);
//		final int status = response.getStatusLine().getStatusCode();
//		   if (status== HttpStatus.SC_OK) {
//			 return response;
//		   }
//
//
//return null;
//}
//
//
//
//
//public HttpResponse deletePaycode(PaycodeClass paycode) throws Exception {
//
//	String transactionRef = paycode.getTransactionRef();
//	String frontEndPartnerId= paycode.getFrontEndPartnerId();
//	String resourceUrl="http://localhost:9080/api/v1/pwm/tokens";
//
//	JSONObject json = new JSONObject();
//
//	json.put("transactionRef", transactionRef);
//	json.put("frontEndPartnerId", frontEndPartnerId);
//
//	String jsonData = json.toString();
//	String httpMethod="DELETE";
//	String signatureMethod = "SHA1";
//
//	HashMap<String, String> extraHeaders = Interswitch.generateSignature(httpMethod, resourceUrl,paycode.getClientId(), paycode.getClientSecret(),signatureMethod);
//	System.out.println("The Extraheaders is :" + extraHeaders);
//
//			HttpClient client = new DefaultHttpClient();
//			System.setProperty("http.proxyHost", "172.16.10.20");
//			System.setProperty("http.port", "8080");
//
//			HttpPost post = new HttpPost(resourceUrl);
//
//
//			post.setHeader("Authorization", extraHeaders.get(AUTHORIZATION));
//			post.setHeader("Timestamp", extraHeaders.get(TIMESTAMP));
//			post.setHeader("Nonce", extraHeaders.get(NONCE));
//			post.setHeader("Signature", extraHeaders.get(SIGNATURE));
//			post.setHeader("SignatureMethod", extraHeaders.get(SIGNATURE_METHOD));
//			post.setHeader("frontEndPartnerId",paycode.getFrontEndPartnerId());
//			post.setHeader("ACCESS_TOKEN", Passport.getClientAccessToken(paycode.getClientId(),paycode.getClientSecret(),ConstantUtils.SANDBOX_PASSPORT_BASE_URL).get("access_token"));
//			post.setHeader("Content-Type","application/json");
//
//
//			StringEntity entity = new StringEntity(jsonData);
//
//			entity.setContentType("application/json");
//			post.setEntity(entity);
//		HttpResponse response = client.execute(post);
//
//	return response;
//
//}
//}
