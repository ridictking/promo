package com.ng.emts.christmasOffer.model.response.querysubscriber;

public class SubscriberOwnership {
    private String ownerEntityType;
    private String ownerEntityId;


    // Getter Methods

    public String getOwnerEntityType() {
        return ownerEntityType;
    }

    public String getOwnerEntityId() {
        return ownerEntityId;
    }

    // Setter Methods

    public void setOwnerEntityType(String ownerEntityType) {
        this.ownerEntityType = ownerEntityType;
    }

    public void setOwnerEntityId(String ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    @Override
    public String toString() {
        return "SubscriberOwnership{" +
                "ownerEntityType='" + ownerEntityType + '\'' +
                ", ownerEntityId='" + ownerEntityId + '\'' +
                '}';
    }
}
