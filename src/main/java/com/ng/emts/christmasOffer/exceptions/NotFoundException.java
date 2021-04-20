package com.ng.emts.christmasOffer.exceptions;


import com.ng.emts.christmasOffer.model.base.ResponseMessage;

/**
 * created by Ridhwan Oladejo, 2020
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5919447122348400126L;
    private String code;
    private String logId;

    public  NotFoundException(){ this.code = ResponseMessage.NOT_FOUND.getCode();}

    public NotFoundException(String message) {
        super(message);
        this.code = ResponseMessage.NOT_FOUND.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
