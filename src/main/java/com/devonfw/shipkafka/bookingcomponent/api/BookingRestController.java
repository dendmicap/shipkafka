package com.devonfw.shipkafka.bookingcomponent.api;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingCode;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import com.devonfw.shipkafka.bookingcomponent.domain.repositories.BookingRepository;
import com.devonfw.shipkafka.bookingcomponent.exceptions.BookingAlreadyConfirmedException;
import com.devonfw.shipkafka.bookingcomponent.exceptions.BookingNotFoundException;
import com.devonfw.shipkafka.bookingcomponent.logic.BookingComponentBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingRestController {

    private final BookingComponentBusinessLogic bookingComponentBusinessLogic;

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingRestController(BookingComponentBusinessLogic bookingComponentBusinessLogic,
                                 BookingRepository bookingRepository) {
        this.bookingComponentBusinessLogic = bookingComponentBusinessLogic;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public Booking getBooking(@PathVariable("id") Long bookingId) throws BookingNotFoundException {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @GetMapping
    public List<Booking> getBookings(@RequestParam(value = "bookingcode", required = false, defaultValue = "") String bookingCode) throws BookingNotFoundException {
        if (bookingCode.isEmpty()) {
            return bookingRepository.findAll();
        } else {
            if (!BookingCode.isValid(bookingCode)) {
                throw new BookingNotFoundException(bookingCode);
            } else {
                return Collections.singletonList(bookingRepository
                        .findByBookingCode(new BookingCode(bookingCode))
                        .orElseThrow(() -> new BookingNotFoundException(bookingCode)));
            }
        }
    }

    @PutMapping(value = "/{id:[\\d]+}/confirm")
    public void confirmBooking(@PathVariable("id") Long bookingId) throws BookingNotFoundException, BookingAlreadyConfirmedException {
        bookingComponentBusinessLogic.confirmBooking(bookingId);
    }
}
