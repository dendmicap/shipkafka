package com.devonfw.shipkafka.bookingcomponent.domain.datatypes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.persistence.Embeddable;
import java.util.Random;

@Value
@Embeddable
public class BookingCode {

    @Getter(AccessLevel.NONE)
    private static final int CODE_LENGTH = 6;

    private final String code;

    public BookingCode() {
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(r.nextInt(36)));
        }
        this.code = code.toString();
    }

    public BookingCode(String bookingCode) {
        if (!isValid(bookingCode)) {
            throw new IllegalArgumentException("Invalid bookingcode format: " + bookingCode);
        }
        this.code = bookingCode;
    }

    public static boolean isValid(String bookingCode) {
        if (bookingCode == null)
            return false;
        else
            return bookingCode.matches("^[0-9A-Z]{" + CODE_LENGTH + "}$");
    }
}