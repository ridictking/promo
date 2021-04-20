package com.ng.emts.christmasOffer.model.data;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChristmasOfferRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pooled")
    @GenericGenerator(
            name = "pooled",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "christmas_generator"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "20"),
                    @Parameter(name = "optimizer", value = "pooled")
            }
    )

    private Long id;
    private String transactionReference;
    private String msisdn;
    private boolean isHaveMagicCode;
    private String magicCode;
    @Column(nullable = true)
    private String magicCodeCategory;
    private LocalDateTime transactionDate;
    @Column(nullable = true)
    private boolean cbsTrxnStatus;



    public ChristmasOfferRequestLog() {
    }

    public ChristmasOfferRequestLog(String transactionReference,
                                    String msisdn,
                                    boolean isHaveMagicCode,
                                    String magicCode,
                                    LocalDateTime transactionDate,
                                    String magicCodeCategory) {
        this.transactionReference = transactionReference;
        this.msisdn = msisdn;
        this.isHaveMagicCode = isHaveMagicCode;
        this.magicCode = magicCode;
        this.transactionDate = transactionDate;
        this.magicCodeCategory = magicCodeCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isHaveMagicCode() {
        return isHaveMagicCode;
    }

    public void setHaveMagicCode(boolean haveMagicCode) {
        isHaveMagicCode = haveMagicCode;
    }

    public String getMagicCode() {
        return magicCode;
    }

    public void setMagicCode(String magicCode) {
        this.magicCode = magicCode;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMagicCodeCategory() {
        return magicCodeCategory;
    }

    public void setMagicCodeCategory(String magicCodeCategory) {
        this.magicCodeCategory = magicCodeCategory;
    }
    public boolean isCbsTrxnStatus() {
        return cbsTrxnStatus;
    }

    public void setCbsTrxnStatus(boolean cbsTrxnStatus) {
        this.cbsTrxnStatus = cbsTrxnStatus;
    }
}
