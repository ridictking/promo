package com.ng.emts.christmasOffer.model.request;


public class PaycodeClass {
private	String amount;
private	String ttid ;
private String msisdn;
private	String frontEndPartnerId;
private	String paymentMethodTypeCode;
private	String paymentMethodCode;
private	String payWithMobileChannel;
private	String tokenLifeTimeInMinutes;
private	String oneTimePin;
private	String codeGenerationChannelProvider;
private	String codeGenerationChannel;
private	String transactionRef;
private String clientId;
private String clientSecret;
private String payCode;
		
public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
	public String getTtid() {
		return ttid;
	}
	public void setTtid(String ttid) {
		this.ttid = ttid;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public String getFrontEndPartnerId() {
		return frontEndPartnerId;
	}
	public void setFrontEndPartnerId(String frontEndPartnerId) {
		this.frontEndPartnerId = frontEndPartnerId;
	}
	public String getPaymentMethodTypeCode() {
		return paymentMethodTypeCode;
	}
	public void setPaymentMethodTypeCode(String paymentMethodTypeCode) {
		this.paymentMethodTypeCode = paymentMethodTypeCode;
	}
	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}
	public String getPayWithMobileChannel() {
		return payWithMobileChannel;
	}
	public void setPayWithMobileChannel(String payWithMobileChannel) {
		this.payWithMobileChannel = payWithMobileChannel;
	}
	public String getTokenLifeTimeInMinutes() {
		return tokenLifeTimeInMinutes;
	}
	public void setTokenLifeTimeInMinutes(String tokenLifeTimeInMinutes) {
		this.tokenLifeTimeInMinutes = tokenLifeTimeInMinutes;
	}
	public String getOneTimePin() {
		return oneTimePin;
	}
	public void setOneTimePin(String oneTimePin) {
		this.oneTimePin = oneTimePin;
		
	}
	
	public String getCodeGenerationChannel() {
		return codeGenerationChannel;
	}
	public void setCodeGenerationChannel(String codeGenerationChannel) {
		this.codeGenerationChannel = codeGenerationChannel;
	}
	public String getCodeGenerationChannelProvider() {
		return codeGenerationChannelProvider;
	}
	public void setCodeGenerationChannelProvider(String codeGenerationChannelProvider) {
		this.codeGenerationChannelProvider = codeGenerationChannelProvider;
	}
	public String getTransactionRef() {
		return transactionRef;
	}
	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
	
		
		
}
