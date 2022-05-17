package com.devonfw.shipkafka.bookingcomponent.domain.entities;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.bookingcomponent.dtos.BookingCreateDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {

    @Setter(AccessLevel.NONE)
    private final Date createdOn = new Date();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date lastUpdatedOn;

    private String ship;

    @Setter(AccessLevel.NONE)
    private BookingStatus bookingStatus = BookingStatus.REQUESTED;

    @Version
    private Long version;

    public Booking(String ship) {

        this.ship = ship;
        this.lastUpdatedOn = new Date();
    }

    public static Booking of(BookingCreateDTO bookingCreateDTO) {
        return new Booking(bookingCreateDTO.ship);
    }

    public void updateBookingStatus(BookingStatus newStatus) {
        bookingStatus = bookingStatus.transition(newStatus);
        lastUpdatedOn = new Date();
    }
}
