/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ng.emts.christmasOffer.exceptions;

import org.springframework.http.HttpStatus;

/**
 *
 * @author victor.akinola
 */
public class BESApiClientException extends AbstractException {

    public BESApiClientException(String message) {
        super(message);
    }

    public BESApiClientException(HttpStatus httpStatus, String message) {
        super(httpStatus.toString().concat("::" + message));
    }
}
