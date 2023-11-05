package org.NotificationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootApplication
public class NotificationService {

  public static void main(String[] args) {
    SpringApplication.run(NotificationService.class, args);
  }
  @KafkaListener(topics = "notificationTopic")
  public void handleNotification(OrderPlacedEvent orderplaced) {
	log.info("The order is placed - {}" , orderplaced.getOrdernumber());
  }

}
