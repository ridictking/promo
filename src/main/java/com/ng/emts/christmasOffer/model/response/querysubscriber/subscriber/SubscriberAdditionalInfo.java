package com.ng.emts.christmasOffer.model.response.querysubscriber.subscriber;

import com.ng.emts.christmasOffer.model.response.querysubscriber.SubscriberInformation;

public  class SubscriberAdditionalInfo {
    private SubscriberInformation subscriberInformation;


    // Getter Methods

    public SubscriberInformation getSubscriberInformation() {
        return subscriberInformation;
    }

    // Setter Methods

    public void setSubscriberInformation(SubscriberInformation subscriberInformationObject) {
        this.subscriberInformation = subscriberInformationObject;
    }

    @Override
    public String toString() {
        return "SubscriberAdditionalInfo{" +
                "subscriberInformation=" + subscriberInformation +
                '}';
    }
}
