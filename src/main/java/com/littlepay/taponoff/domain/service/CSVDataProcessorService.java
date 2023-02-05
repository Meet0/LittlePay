package com.littlepay.taponoff.domain.service;

import com.littlepay.taponoff.domain.model.Tap;
import com.littlepay.taponoff.domain.model.Trip;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CSVDataProcessorService {

    List<Tap> extractDataFrom(String file);
    void createCSVFrom(List<Trip> trips);
}
