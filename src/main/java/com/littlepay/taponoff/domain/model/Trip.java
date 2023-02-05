package com.littlepay.taponoff.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "Trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date started;
    private Date finished;
    private long durationInSecs;
    private String fromStopId;
    private String toStopId;
    private BigDecimal chargeAmount;
    private String companyId;
    private String busId;

    //this will be encrypted, but due to time constraints, it's left as plain text for now.
    private String pan;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Trip(Date started, Date finished, long durationInSecs, String fromStopId, String toStopId, BigDecimal chargeAmount, String companyId, String busId, String pan, Status status) {
        this.started = started;
        this.finished = finished;
        this.durationInSecs = durationInSecs;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
        this.status = status;
    }

    public Trip() {
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return id + " " + sdf.format(started) + " " + sdf.format(finished) + " " + durationInSecs + " " + fromStopId + " " + toStopId + " " + chargeAmount + " " + companyId + " " + busId + " " + pan + " " + status;
    }

    public static class TripBuilder {
        private long id;
        private Date started;
        private Date finished;
        private long durationInSecs;
        private String fromStopId;
        private String toStopId;
        private BigDecimal chargeAmount;
        private String companyId;
        private String busId;
        private String pan;
        private Status status;

        public TripBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public TripBuilder withStarted(Date started) {
            this.started = started;
            return this;
        }

        public TripBuilder withFinished(Date finished) {
            this.finished = finished;
            return this;
        }

        public TripBuilder withDurationInSecs(long durationInSecs) {
            this.durationInSecs = durationInSecs;
            return this;
        }

        public TripBuilder withFromStopId(String fromStopId) {
            this.fromStopId = fromStopId;
            return this;
        }

        public TripBuilder withToStopId(String toStopId) {
            this.toStopId = toStopId;
            return this;
        }

        public TripBuilder withChargeAmount(BigDecimal chargeAmount) {
            this.chargeAmount = chargeAmount;
            return this;
        }

        public TripBuilder withCompanyId(String companyId) {
            this.companyId = companyId;
            return this;
        }

        public TripBuilder withBusId(String busId) {
            this.busId = busId;
            return this;
        }

        public TripBuilder withPan(String pan) {
            this.pan = pan;
            return this;
        }

        public TripBuilder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Trip build() {
            return new Trip(started, finished, durationInSecs, fromStopId, toStopId, chargeAmount, companyId, busId, pan, status);
        }
    }
}
