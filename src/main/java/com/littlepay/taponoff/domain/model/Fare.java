package com.littlepay.taponoff.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Fares")
public class Fare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String origin;
    private String destination;
    private BigDecimal fare;

    public Fare() {
    }

    public BigDecimal getFare() {
        return fare;
    }

    public String getDestination() { return destination; }

    public Fare(String origin, String destination, BigDecimal fare) {
        this.origin = origin;
        this.destination = destination;
        this.fare = fare;
    }
}
