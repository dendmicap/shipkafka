package com.devonfw.shipkafka.bookingcomponent.domain.entities;
import com.devonfw.shipkafka.bookingcomponent.dtos.CustomerCreateDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private List<Booking> bookings = new ArrayList<>();

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Customer of(CustomerCreateDTO customerCreateDTO) {
        return new Customer(
                customerCreateDTO.getFirstName(),
                customerCreateDTO.getLastName()
        );
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}

