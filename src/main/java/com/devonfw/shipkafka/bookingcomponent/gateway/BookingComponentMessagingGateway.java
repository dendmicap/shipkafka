package com.devonfw.shipkafka.bookingcomponent.gateway;

import com.devonfw.shipkafka.bookingcomponent.events.ShipDamagedEvent;
import com.devonfw.shipkafka.bookingcomponent.exceptions.ShipNotFoundException;
import com.devonfw.shipkafka.bookingcomponent.logic.BookingComponentBusinessLogic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
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

    private final Log log = LogFactory.getLog(getClass());

    private final KafkaTemplate<String, Object> template;

    private final BookingComponentBusinessLogic bookingComponentBusinessLogic;

    @Autowired
    public BookingComponentMessagingGateway(@Lazy BookingComponentBusinessLogic bookingComponentBusinessLogic, KafkaTemplate<String, Object> template) {
        this.bookingComponentBusinessLogic = bookingComponentBusinessLogic;
        this.template = template;

        //template.setMessageConverter(new JsonMessageConverter());

    }

    @KafkaListener(id ="shipGroup", topics = "shipDamagedTopic")
    public void listenShipDamaged(ShipDamagedEvent shipDamagedEvent) throws ShipNotFoundException {
        log.info("Received message: "+ shipDamagedEvent.toString());
        bookingComponentBusinessLogic.cancelBookings(shipDamagedEvent.getShip());
    }

    public <T> void sendMessage(String topic, T message) {
        log.info("Sending message " + message.toString());
        template.send(topic, message);
    }

    @Bean
    public NewTopic shipDamagedTopic(){
        return TopicBuilder.name("shipDamagedTopic").build();
    }
}
