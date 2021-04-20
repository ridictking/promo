package com.ng.emts.christmasOffer.model.data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PayCodeLog {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "paycode_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;
    private String payCode;
    private String pin;
    private String msisdn;
    private LocalDateTime dateGenerated;
    private String tokenLifeTime;

    public PayCodeLog() {
    }

    public PayCodeLog(String payCode, String pin, String msisdn, LocalDateTime dateGenerated, String tokenLifeTime) {
        this.payCode = payCode;
        this.pin = pin;
        this.msisdn = msisdn;
        this.dateGenerated = dateGenerated;
        this.tokenLifeTime = tokenLifeTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public LocalDateTime getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(LocalDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public String getTokenLifeTime() {
        return tokenLifeTime;
    }

    public void setTokenLifeTime(String tokenLifeTime) {
        this.tokenLifeTime = tokenLifeTime;
    }
}
