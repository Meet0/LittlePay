package com.littlepay.taponoff.infrastructure;

import com.littlepay.taponoff.domain.model.Tap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TapRepository extends JpaRepository<Tap, Long> {
}
