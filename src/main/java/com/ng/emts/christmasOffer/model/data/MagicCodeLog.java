package com.ng.emts.christmasOffer.model.data;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MAGIC_CODE_LOG")
public class MagicCodeLog {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "magic_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @Column(unique = true)
    private String magicCode;
    private String category;
    private boolean used;
    private LocalDateTime dateUsed;
    @Column(nullable = true)
    private String msisdn;
    @Column(nullable = true)
    private int catCPrice;

    public MagicCodeLog() {
    }

    public MagicCodeLog(String magicCode, String category, boolean used, LocalDateTime dateUsed, String msisdn, int catCPrice) {
        this.magicCode = magicCode;
        this.category = category;
        this.used = used;
        this.dateUsed = dateUsed;
        this.msisdn = msisdn;
        this.catCPrice = catCPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMagicCode() {
        return magicCode;
    }

    public void setMagicCode(String magicCode) {
        this.magicCode = magicCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(LocalDateTime dateUsed) {
        this.dateUsed = dateUsed;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getCatCPrice() {
        return catCPrice;
    }

    public void setCatCPrice(int catCPrice) {
        this.catCPrice = catCPrice;
    }
}
