package com.devonfw.shipkafka.bookingcomponent.domain.repositories;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BookingRepositoryCustomImpl implements BookingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Booking> findConfirmedBookings(Long customerId) {
        return entityManager.createQuery(
                "SELECT b FROM Booking b, Customer c WHERE c.id = :customerId AND b member of c.bookings " +
                        "AND b.bookingStatus = :bookingStatus", Booking.class)
                .setParameter("customerId", customerId)
                .setParameter("bookingStatus", BookingStatus.CONFIRMED)
                .getResultList();
    }
}
