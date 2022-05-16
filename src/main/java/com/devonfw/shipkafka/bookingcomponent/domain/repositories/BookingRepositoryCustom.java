package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;

import java.util.List;

public interface BookingRepositoryCustom {
    List<Booking> findConfirmedBookings(Long customerId);
}
