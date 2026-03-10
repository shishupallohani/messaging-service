package com.assist.messaging.service.mail;

import com.assist.messaging.service.consumer.GrievanceCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class GrievanceMailService {

    private static final Logger log = LoggerFactory.getLogger(GrievanceMailService.class);

    private final JavaMailSender mailSender;
    private final String fixedRecipient;
    private final String mailFrom;

    public GrievanceMailService(
            JavaMailSender mailSender,
            @Value("${app.mail.fixed-recipient}") String fixedRecipient,
            @Value("${app.mail.from}") String mailFrom
    ) {
        this.mailSender = mailSender;
        this.fixedRecipient = fixedRecipient;
        this.mailFrom = mailFrom;
    }

    public void sendGrievanceCreatedMail(GrievanceCreatedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(fixedRecipient);
        message.setSubject("New Grievance Event: " + nullSafe(event.getGrievanceNo()));
        message.setText(buildBody(event));

        try {
            mailSender.send(message);
            log.info("Mail sent to {} for grievanceNo={}", fixedRecipient, event.getGrievanceNo());
        } catch (MailException ex) {
            log.error(
                    "Failed to send grievance mail to {} for grievanceNo={}: {}",
                    fixedRecipient,
                    event.getGrievanceNo(),
                    ex.getMessage()
            );
        }
    }

    private String buildBody(GrievanceCreatedEvent event) {
        StringBuilder body = new StringBuilder();
        body.append("A new grievance event was received.").append('\n').append('\n');
        body.append("Grievance No: ").append(nullSafe(event.getGrievanceNo())).append('\n');
        body.append("Grievance Txn No: ").append(nullSafe(event.getGrievanceTxnNo())).append('\n');
        body.append("Name: ").append(nullSafe(event.getName())).append('\n');
        body.append("Email (from event): ").append(nullSafe(event.getEmail())).append('\n');
        body.append("Created At: ").append(nullSafe(event.getCreatedAt())).append('\n');
        body.append('\n');
        body.append("Note: Mail recipient is fixed to ").append(fixedRecipient).append(".");
        return body.toString();
    }

    private String nullSafe(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}
