package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingCode;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b, Customer c WHERE c.id = :customerId AND b member of c.bookings " +
            "AND b.bookingStatus = com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus.CONFIRMED")
    List<Booking> findConfirmedBookings(Long customerId);

    Optional<Booking> findByBookingCode(BookingCode bookingCode);

    List<Booking> findBookingsByShip(String ship);
}
