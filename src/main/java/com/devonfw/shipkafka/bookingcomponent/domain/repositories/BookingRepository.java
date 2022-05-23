package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b, Customer c WHERE c.id = :customerId AND b member of c.bookings " +
            "AND b.bookingStatus = com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus.CONFIRMED")
    List<Booking> findConfirmedBookings(Long customerId);

    //List<Booking> findBookingsByShip(String ship);
    List<Booking> findBookingsByShip(Ship ship);
}
