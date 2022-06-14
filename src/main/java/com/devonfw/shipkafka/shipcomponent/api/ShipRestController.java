package com.devonfw.shipkafka.shipcomponent.api;

import com.devonfw.shipkafka.bookingcomponent.logic.BookingComponentBusinessLogic;
import com.devonfw.shipkafka.common.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.common.domain.entities.Booking;
import com.devonfw.shipkafka.common.events.ShipDamagedEvent;
import com.devonfw.shipkafka.common.exceptions.BookingAlreadyConfirmedException;
import com.devonfw.shipkafka.shipcomponent.dtos.IdDTO;
import com.devonfw.shipkafka.shipcomponent.dtos.ShipCreateDTO;
import com.devonfw.shipkafka.shipcomponent.dtos.ShipUpdateDTO;
import com.devonfw.shipkafka.shipcomponent.exceptions.ShipDamagedException;
import com.devonfw.shipkafka.shipcomponent.exceptions.ShipNotFoundException;
import com.devonfw.shipkafka.shipcomponent.domain.entities.Ship;
import com.devonfw.shipkafka.shipcomponent.domain.repositories.ShipRepository;
import com.devonfw.shipkafka.shipcomponent.logic.ShipComponentLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/ships")
public class ShipRestController {

    private final ShipComponentLogic shipComponentLogic;

    // testing retry
    private final BookingComponentBusinessLogic bookingComponentBusinessLogic;

    private final ShipRepository shipRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ShipRestController.class);

    private boolean shipDamaged;

    @Autowired
    public ShipRestController(ShipComponentLogic shipComponentLogic, ShipRepository shipRepository, BookingComponentBusinessLogic bookingComponentBusinessLogic) {
        this.shipComponentLogic = shipComponentLogic;
        this.shipRepository = shipRepository;
        this.bookingComponentBusinessLogic = bookingComponentBusinessLogic;
    }

    @GetMapping
    public List<Ship> getShips() {
        return shipRepository.findAll();
    }

    @GetMapping(value = "/{id:\\d+}")
    public Ship getShip(@PathVariable("id") Long shipId) throws ShipNotFoundException {
        return shipRepository
                .findById(shipId)
                .orElseThrow(() -> new ShipNotFoundException(shipId));
    }

    @PostMapping(value = "/{id:\\d+}/damage")
    @ResponseStatus(HttpStatus.OK)
    public ShipDamagedEvent postDamagedShip(@PathVariable("id") Long shipId) {
        ShipDamagedEvent shipDamagedEvent = new ShipDamagedEvent(shipId);
        shipComponentLogic.sendMessage("ship-damaged", shipDamagedEvent);
        return shipDamagedEvent;
    }

    //TODO: move it somewhere else
    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 5_000, maxDelay = 30_000, multiplier = 2))
    @KafkaListener(id = "bookings", topics = "bookings", groupId = "ship")
    public void onBookingEvent(Booking booking) throws ShipNotFoundException, BookingAlreadyConfirmedException, ShipDamagedException {

        // Only for testing purpose
        Ship ship = shipRepository.findById(booking.getShipId()).orElseThrow(() -> new ShipNotFoundException(booking.getShipId()));

        // testing retry
        if (booking.getBookingStatus().equals(BookingStatus.REQUESTED) && ship.getDamaged()) {
            //shipDamaged = ship.isDamaged();
            LOG.info("FAILED");
            bookingComponentBusinessLogic.cancelBookings(booking.getId());
            throw new ShipDamagedException((long) 22);
        } else {
            LOG.info("Received: {}", booking);
            shipComponentLogic.confirmBooking(booking);
        }


        /*
        LOG.info("Received: {}", booking);
        shipComponentLogic.confirmBooking(booking);
         */


    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO addShip(@Valid @RequestBody ShipCreateDTO shipCreateDTO){

        return new IdDTO(shipRepository.save(Ship.of(shipCreateDTO)).getId());
    }

    @PutMapping
    public void updateShipCondition(@Valid @RequestBody ShipUpdateDTO shipUpdateDTO) throws ShipNotFoundException{

        Ship shipToUpdate = shipRepository
                .findById(shipUpdateDTO.getId())
                .orElseThrow(() -> new ShipNotFoundException(shipUpdateDTO.getId()));

        if (!shipUpdateDTO.getDamaged()) {
            shipToUpdate.setDamaged(false);
        } else
        {
            shipToUpdate.setDamaged(true);
        }

        shipRepository.save(shipToUpdate);
    }
}
