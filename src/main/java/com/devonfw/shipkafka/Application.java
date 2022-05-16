package com.devonfw.shipkafka;

import com.devonfw.shipkafka.bookingcomponent.domain.datatypes.BookingStatus;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Booking;
import com.devonfw.shipkafka.bookingcomponent.domain.entities.Customer;
import com.devonfw.shipkafka.bookingcomponent.domain.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SpringBootApplication
public class Application implements HealthIndicator {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public Health health() {
		return Health.up().build();
	}
}


@Component
@Profile("default")
class PopulateTestDataRunner implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	@Autowired
	public PopulateTestDataRunner(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(String... args) {
		Arrays.asList(
						"Miller,Doe,Smith".split(","))
				.forEach(
						name -> customerRepository.save(new Customer("Jane", name))
				);

		Customer customer = new Customer("Max", "Muster");
		Booking booking = new Booking("scandlines1");
		customer.getBookings().add(booking);
		booking = new Booking("scandlines2");
		booking.updateBookingStatus(BookingStatus.CONFIRMED);
		customer.getBookings().add(booking);

		customerRepository.save(customer);
	}
}
