package com.devonfw.shipkafka.bookingcomponent.logic;
import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Customer;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import com.devonfw.shipkafka.bookingcomponent.domain.repositories.BookingRepository;
import com.devonfw.shipkafka.bookingcomponent.domain.repositories.CustomerRepository;
import com.devonfw.shipkafka.bookingcomponent.dtos.BookingCreateDTO;
import com.devonfw.shipkafka.bookingcomponent.exceptions.BookingAlreadyConfirmedException;
import com.devonfw.shipkafka.bookingcomponent.exceptions.BookingNotFoundException;
import com.devonfw.shipkafka.bookingcomponent.exceptions.CustomerNotFoundException;
import com.devonfw.shipkafka.bookingcomponent.exceptions.ShipNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingComponentBusinessLogic {

    private final CustomerRepository customerRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingComponentBusinessLogic(CustomerRepository customerRepository,
                                         BookingRepository bookingRepository){
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(rollbackFor = {CustomerNotFoundException.class})
    public Booking addBooking(Long customerId, BookingCreateDTO bookingCreateDTO) throws CustomerNotFoundException{
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            Booking booking = bookingRepository.save(Booking.of(bookingCreateDTO));

            customer.addBooking(booking);
            customerRepository.save(customer);

            return booking;
        } else {
            throw new CustomerNotFoundException(customerId);
        }
    }

    @Transactional(rollbackFor = {BookingNotFoundException.class, BookingAlreadyConfirmedException.class})
    public void confirmBooking(Long bookingId) throws BookingNotFoundException, BookingAlreadyConfirmedException {

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new BookingAlreadyConfirmedException(bookingId);
        }

        booking.updateBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookings(Long customerId, Boolean onlyConfirmed) throws CustomerNotFoundException {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (onlyConfirmed) {
            return bookingRepository.findConfirmedBookings(customerId);
        } else {
            return customer.getBookings();
        }
    }

    // public void cancelBookings(String ship)
    @Transactional(rollbackFor = {ShipNotFoundException.class})
    public void cancelBookings(Ship ship)throws ShipNotFoundException{
        List<Booking> bookings = bookingRepository.findBookingsByShip(ship);

        if (bookings.isEmpty()){
            throw new ShipNotFoundException(ship);
        }

        for (Booking booking : bookings) {
            booking.updateBookingStatus(BookingStatus.CANCELED);
            bookingRepository.save(booking);
        }
    }
}
