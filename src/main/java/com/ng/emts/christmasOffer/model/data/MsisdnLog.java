package com.ng.emts.christmasOffer.model.data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MsisdnLog {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "msisdn_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;
    private String msisdn;
    private boolean magicCode;

    public MsisdnLog(String msisdn, boolean magicCode) {
        this.msisdn = msisdn;
        this.magicCode = magicCode;
    }

    public MsisdnLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isMagicCode() {
        return magicCode;
    }

    public void setMagicCode(boolean magicCode) {
        this.magicCode = magicCode;
    }
}
