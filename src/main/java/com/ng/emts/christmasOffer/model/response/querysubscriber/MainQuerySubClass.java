package com.ng.emts.christmasOffer.model.response.querysubscriber;

public class MainQuerySubClass {
    private String command;
    private String transactionId;
    private String returnCode;
    private String returnMsg;
    private Subscriber subscriber;


    // Getter Methods

    public String getCommand() {
        return command;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    // Setter Methods

    public void setCommand(String command) {
        this.command = command;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setSubscriber(Subscriber subscriberObject) {
        this.subscriber = subscriberObject;
    }

    @Override
    public String toString() {
        return "MainQuerySubClass{" +
                "command='" + command + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", subscriber=" + subscriber +
                '}';
    }
}

