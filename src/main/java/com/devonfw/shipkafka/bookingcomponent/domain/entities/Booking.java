package com.devonfw.shipkafka.bookingcomponent.domain.entities;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingCode;
import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.bookingcomponent.dtos.BookingCreateDTO;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {

    @Setter(AccessLevel.NONE)
    @Column(unique = true, nullable = false, length = 100)
    private final BookingCode bookingCode = new BookingCode();
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
