package com.littlepay.taponoff.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Taps")
public class Tap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date dateTimeUTC;

    @Enumerated(EnumType.STRING)
    private TapType tapType;
    private String stopId;
    private String companyId;
    private String busId;
    private String pan;

    public Tap(Date dateTimeUTC, TapType tapType, String stopId, String companyId, String busId, String pan) {
        this.dateTimeUTC = dateTimeUTC;
        this.tapType = tapType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
    }


    public Tap() {

    }

    public Date getDateTimeUTC() {
        return dateTimeUTC;
    }

    public String getStopId() {
        return stopId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getBusId() {
        return busId;
    }

    public String getPan() {
        return pan;
    }
}
