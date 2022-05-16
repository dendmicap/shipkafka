package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByLastName(String lastName);

    Optional<Customer> findByBookingsContaining(Booking booking);
}
