package com.littlepay.taponoff.domain.service;

import com.littlepay.taponoff.domain.model.Fare;
import com.littlepay.taponoff.domain.model.Status;
import com.littlepay.taponoff.domain.model.Tap;
import com.littlepay.taponoff.domain.model.Trip;
import com.littlepay.taponoff.infrastructure.FareRepository;
import com.littlepay.taponoff.infrastructure.TapRepository;
import com.littlepay.taponoff.infrastructure.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FareCalculatorServiceImpl implements FareCalculatorService {

    private final TapRepository tapRepository;
    private final TripRepository tripRepository;
    private final FareRepository fareRepository;
    private final CSVDataProcessorService dataProcessorService;
    private final HashMap<String, Tap> tapMap = new HashMap<>();
    final List<Trip> trips = new ArrayList<>();

    @Autowired
    public FareCalculatorServiceImpl(TapRepository tapRepository, TripRepository tripRepository, FareRepository fareRepository) {
        this.tapRepository = tapRepository;
        this.tripRepository = tripRepository;
        this.fareRepository = fareRepository;
        this.dataProcessorService = new CSVDataProcessorServiceImpl();
    }

    @Override
    public void calculateFaresFrom(String filePath) {
        List<Tap> extractedTapsData = dataProcessorService.extractDataFrom(filePath);

        for (Tap tap: extractedTapsData) {
            calculateFare(tap);
            saveTapObjectAsync(tap);
        }

        if (!tapMap.isEmpty()) {
            calculateFareForInCompleteTrips();
        }

        if (!trips.isEmpty()) {
            dataProcessorService.createCSVFrom(trips);
        }
    }

    void calculateFare(Tap tap) {
        Tap prevMatchingTap = tapMap.get(tap.getPan());
        if (prevMatchingTap != null) {
            Trip trip = buildTripFrom(tap, prevMatchingTap);
            updateTripWithChargeAndStatus(trip);
            trips.add(trip);
            tapMap.remove(tap.getPan());
            saveTripObjectAsync(trip);
        } else {
            tapMap.put(tap.getPan(), tap);
        }
    }

    void updateTripWithChargeAndStatus(Trip trip) {
        if (!trip.getFromStopId().equals(trip.getToStopId())) {
            BigDecimal chargeAmount = fareRepository.findByOriginAndDestination(trip.getFromStopId(), trip.getToStopId()).getFare();
            trip.setChargeAmount(chargeAmount);
            trip.setStatus(Status.COMPLETED);
        } else {
            trip.setChargeAmount(BigDecimal.ZERO);
            trip.setStatus(Status.CANCELLED);
        }
    }

    Trip buildTripFrom(Tap tap, Tap prevMatchingTap) {
        return new Trip.TripBuilder()
                .withStarted(prevMatchingTap.getDateTimeUTC())
                .withFinished(tap.getDateTimeUTC())
                .withDurationInSecs(calculateDurationInSecs(tap.getDateTimeUTC(), prevMatchingTap.getDateTimeUTC()))
                .withFromStopId(prevMatchingTap.getStopId())
                .withToStopId(tap.getStopId())
                .withCompanyId(tap.getCompanyId())
                .withBusId(tap.getBusId())
                .withPan(tap.getPan())
                .build();
    }

    long calculateDurationInSecs(Date from, Date to) {
        return (from.getTime() - to.getTime())/ 1000;
    }

    private void saveTripObjectAsync(Trip trip) {
        CompletableFuture.runAsync(() -> tripRepository.save(trip));
    }

    private void saveTapObjectAsync(Tap tap) {
        CompletableFuture.runAsync(() -> tapRepository.save(tap));
    }

    void calculateFareForInCompleteTrips() {
        for (Tap tap : tapMap.values()) {
            Fare lastStopInRoute = fareRepository.findTopByOrderByIdDesc();
            Trip trip = new Trip.TripBuilder()
                    .withStarted(tap.getDateTimeUTC())
                    .withFinished(tap.getDateTimeUTC())
                    .withDurationInSecs(0)
                    .withFromStopId(tap.getStopId())
                    .withToStopId(lastStopInRoute.getDestination())
                    .withChargeAmount(lastStopInRoute.getFare())
                    .withCompanyId(tap.getCompanyId())
                    .withBusId(tap.getBusId())
                    .withPan(tap.getPan())
                    .withStatus(Status.INCOMPLETE).build();
            trips.add(trip);
            saveTripObjectAsync(trip);
            tapMap.remove(tap.getPan());
        }
    }
}
