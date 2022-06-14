package com.devonfw.shipkafka.shipcomponent.domain.entities;

import com.devonfw.shipkafka.shipcomponent.dtos.ShipCreateDTO;
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

    private String name;

    private int availableContainers;

    // Testing retry
    private Boolean damaged;

    public Ship(String name, int availableContainers, Boolean damaged){
        this.name = name;
        this.availableContainers = availableContainers;
        this.damaged = damaged;
    }

    // Testing retry
    public static Ship of(ShipCreateDTO shipCreateDTO) {
        return new Ship(
                shipCreateDTO.getShipName(),
                shipCreateDTO.getAvailableContainers(),
                shipCreateDTO.isDamaged()
        );
    }
}
