package com.devonfw.shipkafka.bookingcomponent.exceptions;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.BAD_REQUEST)
@EqualsAndHashCode(callSuper = false)
public class ShipNotFoundException extends Exception {

    // private final String ship;
    private final Ship ship;

    public ShipNotFoundException(Ship ship) {
        super(String.format("Ship %s was not found.", ship));

        this.ship = ship;
    }
}