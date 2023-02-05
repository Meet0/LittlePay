package com.littlepay.taponoff.domain.service;

import org.springframework.stereotype.Service;

@Service
public interface FareCalculatorService {

   void calculateFaresFrom(String filePath);
}
