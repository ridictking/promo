package com.ng.emts.christmasOffer.model.request;

public class MainQuerySubClassRequest {
    private String serviceNumber;
    private String appName;

    public MainQuerySubClassRequest(String serviceNumber, String appName) {
        this.serviceNumber = serviceNumber;
        this.appName = appName;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
