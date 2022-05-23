package com.devonfw.shipkafka.bookingcomponent.dtos;

import com.devonfw.shipkafka.bookingcomponent.domain.entities.Ship;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookingCreateDTO {

    @Size(min = 1, max = 20)
    @NotNull
    public Ship ship;             // previously public String ship
}
