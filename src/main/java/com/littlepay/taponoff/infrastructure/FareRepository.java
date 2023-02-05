package com.littlepay.taponoff.infrastructure;

import com.littlepay.taponoff.domain.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {

    Fare findByOriginAndDestination(String origin, String destination);

    Fare findTopByOrderByIdDesc();
}
