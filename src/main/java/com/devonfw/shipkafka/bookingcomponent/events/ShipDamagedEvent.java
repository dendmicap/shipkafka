package com.devonfw.shipkafka.bookingcomponent.events;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipDamagedEvent {

    // private String ship;
    private Ship ship;
}
