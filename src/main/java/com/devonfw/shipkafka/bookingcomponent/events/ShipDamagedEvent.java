package com.devonfw.shipkafka.bookingcomponent.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipDamagedEvent {

    private String ship;
}
