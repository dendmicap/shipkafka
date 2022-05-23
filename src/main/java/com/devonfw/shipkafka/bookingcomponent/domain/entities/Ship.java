package com.devonfw.shipkafka.bookingcomponent.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shipName;

    private int availableContainer;

    public Ship(String shipName, Integer availableContainer) {
        this.shipName = shipName;
        this.availableContainer = availableContainer;
    }
}
