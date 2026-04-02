package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.NotificationMessage;
import org.example.publisher.NotificationProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationProducer notificationProducer;

    @PostMapping()
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage notificationMessage){
        notificationProducer.sendMessage(notificationMessage);
        return ResponseEntity.ok("Success!");
    }
}
