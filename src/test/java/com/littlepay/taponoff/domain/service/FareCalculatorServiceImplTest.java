package com.littlepay.taponoff.domain.service;

import com.littlepay.taponoff.domain.model.Fare;
import com.littlepay.taponoff.domain.model.Status;
import com.littlepay.taponoff.domain.model.Tap;
import com.littlepay.taponoff.domain.model.Trip;
import com.littlepay.taponoff.infrastructure.FareRepository;
import com.littlepay.taponoff.infrastructure.TapRepository;
import com.littlepay.taponoff.infrastructure.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FareCalculatorServiceImplTest {

    FareCalculatorServiceImpl fareCalculatorService;

    @Mock
    TapRepository tapRepository;

    @Mock
    TripRepository tripRepository;

    @Mock
    FareRepository fareRepository;

    private final List<Fare> FARES = getSampleFares();
    private final String MATCHING_TAPS_ONLY_FILENAME = getClass().getClassLoader().getResource( "data.csv").getPath();
    private final String MIXED_TAPS_ONLY_FILENAME = getClass().getClassLoader().getResource( "IncompleteTaps.csv").getPath();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        fareCalculatorService = new FareCalculatorServiceImpl(tapRepository, tripRepository, fareRepository);
        when(fareRepository.findByOriginAndDestination(anyString(), anyString())).thenReturn(FARES.get(0));
    }

    private List<Fare> getSampleFares() {
        List<Fare> fares = new ArrayList<>();

        fares.add(new Fare("Stop1", "Stop2", BigDecimal.valueOf(3.25)));
        fares.add(new Fare("Stop2", "Stop3", BigDecimal.valueOf(5.50)));
        fares.add(new Fare("Stop1", "Stop3", BigDecimal.valueOf(7.30)));

        return fares;
    }

    @Test
    void calculateFaresFrom_matchingTaps_shouldCalculateFaresSuccessfully() {
        fareCalculatorService.calculateFaresFrom(MATCHING_TAPS_ONLY_FILENAME);

        verify(fareRepository, times(1)).findByOriginAndDestination(anyString(), anyString());
        verify(tapRepository, times(2)).save(any(Tap.class));
        verify(tripRepository, times(1)).save(any(Trip.class));
        assertEquals(1, fareCalculatorService.trips.size());
        Trip matchingTapTrip = fareCalculatorService.trips.get(0);
        assertEquals(BigDecimal.valueOf(3.25), matchingTapTrip.getChargeAmount());
        assertEquals(Status.COMPLETED, matchingTapTrip.getStatus());
        assertEquals("Stop1", matchingTapTrip.getFromStopId());
        assertEquals("Stop2", matchingTapTrip.getToStopId());
    }

    @Test
    void calculateFaresFrom_incompleteTrip_shouldCalculateFaresSuccessfully() {
        when(fareRepository.findTopByOrderByIdDesc()).thenReturn(FARES.get(FARES.size()-1));

        fareCalculatorService.calculateFaresFrom(MIXED_TAPS_ONLY_FILENAME);

        verify(fareRepository, times(1)).findByOriginAndDestination(anyString(), anyString());
        verify(fareRepository, times(1)).findTopByOrderByIdDesc();
        verify(tapRepository, times(3)).save(any(Tap.class));
        verify(tripRepository, times(2)).save(any(Trip.class));
        assertEquals(2, fareCalculatorService.trips.size());
        Trip incompleteTapTrip = fareCalculatorService.trips.get(1);
        assertEquals(BigDecimal.valueOf(7.30), incompleteTapTrip.getChargeAmount());
        assertEquals(Status.INCOMPLETE, incompleteTapTrip.getStatus());
        assertEquals("Stop1", incompleteTapTrip.getFromStopId());
        assertEquals("Stop3", incompleteTapTrip.getToStopId());
    }

    @Test
    void updateTripWithChargeAndStatus_completeTrip_shouldUpdateChargeAndStatusSuccessfully() {
        Trip trip = new Trip.TripBuilder()
                .withStarted(Date.from(Instant.now()))
                .withFinished(Date.from(Instant.now()))
                .withPan("12345")
                .withBusId("bus1")
                .withCompanyId("company1")
                .withDurationInSecs(0)
                .withFromStopId("Stop1")
                .withToStopId("Stop2")
                .build();

        fareCalculatorService.updateTripWithChargeAndStatus(trip);

        assertEquals(Status.COMPLETED, trip.getStatus());
        assertEquals(BigDecimal.valueOf(3.25), trip.getChargeAmount());
    }

    @Test
    void updateTripWithChargeAndStatus_cancelledTrip_shouldUpdateChargeAndStatusSuccessfully() {
        Trip trip = new Trip.TripBuilder()
                .withStarted(Date.from(Instant.now()))
                .withFinished(Date.from(Instant.now()))
                .withPan("12345")
                .withBusId("bus1")
                .withCompanyId("company1")
                .withDurationInSecs(0)
                .withFromStopId("Stop1")
                .withToStopId("Stop1")
                .build();

        fareCalculatorService.updateTripWithChargeAndStatus(trip);

        assertEquals(Status.CANCELLED, trip.getStatus());
        assertEquals(BigDecimal.ZERO, trip.getChargeAmount());
    }


}