package com.assist.messaging.service.controller;

import com.assist.messaging.service.consumer.GrievanceCreatedEvent;
import com.assist.messaging.service.mail.GrievanceMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Testing purpose only:
 * This controller exists only for manual verification from Postman/local testing.
 * Do not expose this endpoint in production environments.
 */
@RestController
@RequestMapping("/api/test/messaging")
public class MessagingTestController {

    private static final Logger log = LoggerFactory.getLogger(MessagingTestController.class);

    private final GrievanceMailService grievanceMailService;

    public MessagingTestController(GrievanceMailService grievanceMailService) {
        this.grievanceMailService = grievanceMailService;
    }

    @PostMapping("/grievance-created")
    public ResponseEntity<Map<String, Object>> triggerGrievanceCreatedMail(
            @RequestBody GrievanceCreatedEvent event
    ) {
        log.info("Received manual test request for grievance-created event: {}", event);
        grievanceMailService.sendGrievanceCreatedMail(event);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "TRIGGERED");
        response.put("message", "Test request accepted. Check application logs and recipient inbox.");
        response.put("grievanceNo", event.getGrievanceNo());
        return ResponseEntity.ok(response);
    }
}
