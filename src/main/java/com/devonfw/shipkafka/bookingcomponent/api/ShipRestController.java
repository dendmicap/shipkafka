package com.devonfw.shipkafka.bookingcomponent.api;

import com.devonfw.shipkafka.bookingcomponent.domain.repositories.ShipRepository;
import com.devonfw.shipkafka.bookingcomponent.events.ShipDamagedEvent;
import com.devonfw.shipkafka.bookingcomponent.gateway.BookingComponentMessagingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/ships")
public class ShipRestController {

    private final BookingComponentMessagingGateway bookingComponentMessagingGateway;

    // TODO: add ShipRepository
    private final ShipRepository shipRepository;

    @Autowired
    public ShipRestController(BookingComponentMessagingGateway bookingComponentMessagingGateway, ShipRepository shipRepository) {
        this.bookingComponentMessagingGateway = bookingComponentMessagingGateway;
        this.shipRepository = shipRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ShipDamagedEvent postDamagedShip(@Valid @RequestBody ShipDamagedEvent shipDamagedEvent) {
        bookingComponentMessagingGateway.sendMessage("shipDamagedTopic", shipDamagedEvent);
        return shipDamagedEvent;
    }
}
