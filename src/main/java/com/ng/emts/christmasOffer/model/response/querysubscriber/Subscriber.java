package com.ng.emts.christmasOffer.model.response.querysubscriber;

import com.ng.emts.christmasOffer.model.response.querysubscriber.subscriber.SubscriberAdditionalInfo;

public class Subscriber {
    private BusinessOwnership businessOwnership;
    private SubscriberOwnership subscriberOwnership;
    private UseCustomer useCustomer;
    private SubscriberAdditionalInfo subscriber;


    // Getter Methods

    public BusinessOwnership getBusinessOwnership() {
        return businessOwnership;
    }

    public SubscriberOwnership getSubscriberOwnership() {
        return subscriberOwnership;
    }

    public UseCustomer getUseCustomer() {
        return useCustomer;
    }

    public SubscriberAdditionalInfo getSubscriber() {
        return subscriber;
    }

    // Setter Methods

    public void setBusinessOwnership(BusinessOwnership businessOwnershipObject) {
        this.businessOwnership = businessOwnershipObject;
    }

    public void setSubscriberOwnership(SubscriberOwnership subscriberOwnershipObject) {
        this.subscriberOwnership = subscriberOwnershipObject;
    }

    public void setUseCustomer(UseCustomer useCustomerObject) {
        this.useCustomer = useCustomerObject;
    }

    public void setSubscriber(SubscriberAdditionalInfo subscriberObject) {
        this.subscriber = subscriberObject;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "businessOwnership=" + businessOwnership +
                ", subscriberOwnership=" + subscriberOwnership +
                ", useCustomer=" + useCustomer +
                ", subscriber=" + subscriber +
                '}';
    }
}
