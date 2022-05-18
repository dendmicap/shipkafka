package com.devonfw.shipkafka.bookingcomponent.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ship {

    @Id
    @GeneratedValue
    private Long id;

    private String shipName;

    private Integer availableContainer;

    public Ship(String shipName, Integer availableContainer) {
        this.shipName = shipName;
        this.availableContainer = availableContainer;
    }
}
