package com.devonfw.shipkafka.shipcomponent.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ShipUpdateDTO {

    @NotNull
    private Long id;

    @NotNull
    private boolean damaged;
}