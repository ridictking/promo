package com.ng.emts.christmasOffer.model.response.querysubscriber;

import java.util.List;

public class SubscriberInformation {
    private String id;
    private String brandId;
    private String serviceNumber;
    private String networkType;
    private String level;
    private String paymentType;
    private String status;
    private String statusReason;
    private String statusTime;
    private String activeDate;
    private String effectiveDate;
    private String expiryDate;
    private List<Property> property;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getLevel() {
        return level;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public List<Property> getProperty() {
        return property;
    }
    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "SubscriberInformation{" +
                "id='" + id + '\'' +
                ", brandId='" + brandId + '\'' +
                ", serviceNumber='" + serviceNumber + '\'' +
                ", networkType='" + networkType + '\'' +
                ", level='" + level + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", status='" + status + '\'' +
                ", statusReason='" + statusReason + '\'' +
                ", statusTime='" + statusTime + '\'' +
                ", activeDate='" + activeDate + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", property=" + property +
                '}';
    }
}
