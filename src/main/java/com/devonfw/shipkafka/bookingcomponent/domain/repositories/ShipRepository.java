package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {

}
