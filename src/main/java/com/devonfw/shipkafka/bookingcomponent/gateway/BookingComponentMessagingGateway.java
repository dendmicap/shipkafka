package com.devonfw.shipkafka.bookingcomponent.gateway;

import com.devonfw.shipkafka.bookingcomponent.exceptions.BookingNotFoundException;
import com.devonfw.shipkafka.common.domain.entities.Booking;
import com.devonfw.shipkafka.common.events.ShipDamagedEvent;
import com.devonfw.shipkafka.shipcomponent.exceptions.ShipNotFoundException;
import com.devonfw.shipkafka.bookingcomponent.logic.BookingComponentBusinessLogic;


import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Implements the Messaging Gateway Pattern
 *
 * @see <a href="https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessagingGateway.html">Messaging Gateway Pattern</a>
 */
@Service
public class BookingComponentMessagingGateway {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final KafkaTemplate<String, Object> template;
    private final KafkaTemplate<Long, Object> longTemplate;


    private final BookingComponentBusinessLogic bookingComponentBusinessLogic;

    @Autowired
    public BookingComponentMessagingGateway(@Lazy BookingComponentBusinessLogic bookingComponentBusinessLogic, KafkaTemplate<String, Object> template, KafkaTemplate<Long, Object> longTemplate) {
        this.bookingComponentBusinessLogic = bookingComponentBusinessLogic;
        this.template = template;
        this.longTemplate = longTemplate;
    }

    @KafkaListener(id ="ship-damaged", topics = "ship-damaged", groupId = "booking")
    public void listenShipDamaged(ShipDamagedEvent shipDamagedEvent) throws ShipNotFoundException {
        LOG.info("Received message: {}", shipDamagedEvent.toString());
        bookingComponentBusinessLogic.cancelBookings(shipDamagedEvent.getShipId());
    }

    @KafkaListener(id ="ship-bookings", topics = "ship-bookings", groupId = "booking")
    public void listenBooking(Booking booking) throws BookingNotFoundException {
        LOG.info("Received message: {}", booking.toString());
        bookingComponentBusinessLogic.processBooking(booking);
    }



    public <T> void sendMessage(String topic, T message) {
        LOG.info("Sending message : {}", message.toString());
        template.send(topic, message);
    }

    public <T> void sendMessage(String topic, Long key, T message) {
        LOG.info("Sending message : {}", message.toString());
        longTemplate.send(topic, key, message);
    }

    @Bean
    public NewTopic bookings() {
        return TopicBuilder.name("bookings")
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic shipTopic() {
        return TopicBuilder.name("ship-bookings")
                .partitions(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic shipDamagedTopic(){
        return TopicBuilder.name("shipDamagedTopic").build();
    }

}
