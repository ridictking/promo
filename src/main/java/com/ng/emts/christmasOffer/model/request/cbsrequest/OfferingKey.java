package com.ng.emts.christmasOffer.model.request.cbsrequest;

public class OfferingKey {
    private String offeringCode;

    private String purchaseSeq;

    public void setOfferingCode(String offeringCode){
        this.offeringCode = offeringCode;
    }
    public String getOfferingCode(){
        return this.offeringCode;
    }
    public void setPurchaseSeq(String purchaseSeq){
        this.purchaseSeq = purchaseSeq;
    }
    public String getPurchaseSeq(){
        return this.purchaseSeq;
    }
}
