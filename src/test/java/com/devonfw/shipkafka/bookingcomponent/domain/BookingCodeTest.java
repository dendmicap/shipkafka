package com.devonfw.shipkafka.bookingcomponent.domain;

import com.devonfw.shipkafka.Application;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = "testing")
class BookingCodeTest {

    private ArrayList<String> codes = new ArrayList<>();

    @Test
    void createBookingCodeSuccess() {
        Assertions.assertThat(new Booking("Mein Schiff").getBookingCode().getCode()).containsPattern(Pattern.compile("[0-9A-Z]{6,6}"));
    }

    // just for fun, not really useful
    @RepeatedTest(50)
    void createUniqueBookingCodeSuccess() {
        String code = new Booking("Mein Schiff").getBookingCode().getCode();
        assertThat(codes.contains(code)).isFalse();
        codes.add(code);
    }
}
