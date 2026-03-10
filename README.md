# Messaging Service

Messaging Service is a Spring Boot application that listens to Kafka grievance events and sends email notifications.

It is designed to work as an async notification layer for a grievance workflow.

## Features

- Consumes `grievance-created` events from Kafka.
- Parses event payload into `GrievanceCreatedEvent`.
- Logs Kafka metadata (key, partition, offset) for traceability.
- Sends formatted email notifications through SMTP.

## Event Flow

1. A producer publishes a grievance event to Kafka topic `grievance-created`.
2. `GrievanceEventConsumer` listens to the topic.
3. The event is passed to `GrievanceMailService`.
4. Service sends an email with grievance details to configured recipient.

## Tech Stack

- Java 25
- Spring Boot 4.0.3
- Spring Kafka
- Spring Mail
- Maven Wrapper

## Project Structure

```text
src/main/java/com/assist/messaging/service
|-- MessagingServiceApplication.java
|-- consumer/
|   |-- GrievanceEventConsumer.java
|   `-- GrievanceCreatedEvent.java
`-- mail/
    `-- GrievanceMailService.java
```

## Configuration

Main configuration is in `src/main/resources/application.properties`.

### Important Environment Variables

| Variable | Purpose | Default |
| --- | --- | --- |
| `SPRING_SERVER_PORT` | App port | `8082` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka broker(s) | `localhost:9092` |
| `SPRING_KAFKA_CONSUMER_GROUP` | Kafka consumer group | `grievance-messaging-consumer` |
| `SPRING_KAFKA_CONSUMER_AUTO_OFFSET_RESET` | Consumer offset policy | `earliest` |
| `APP_KAFKA_TOPIC_GRIEVANCE_CREATED` | Kafka topic name | `grievance-created` |
| `APP_MAIL_FROM` | Sender email | fallback to `SPRING_MAIL_USERNAME` |
| `SPRING_MAIL_HOST` | SMTP host | `smtp.gmail.com` |
| `SPRING_MAIL_PORT` | SMTP port | `587` |
| `SPRING_MAIL_USERNAME` | SMTP username | empty |
| `SPRING_MAIL_PASSWORD` | SMTP password/app password | empty |
| `SPRING_MAIL_SMTP_AUTH` | SMTP auth enabled | `true` |
| `SPRING_MAIL_SMTP_STARTTLS` | TLS enabled | `true` |

`app.mail.fixed-recipient` is currently set in `application.properties`. Update it before production use.

## Prerequisites

- JDK 25 installed
- Kafka running locally or remotely
- SMTP account credentials (for Gmail, prefer App Password)

## Run Locally

1. Start Kafka and create topic (if not already created):

```bash
kafka-topics --create \
  --topic grievance-created \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1
```

2. Export required environment variables:

```bash
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export SPRING_MAIL_USERNAME=your-email@example.com
export SPRING_MAIL_PASSWORD=your-smtp-password
export APP_MAIL_FROM=your-email@example.com
```

3. Run the app:

```bash
./mvnw spring-boot:run
```

For Windows:

```bat
mvnw.cmd spring-boot:run
```

## Test the Consumer

Publish a sample message to Kafka:

```bash
kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic grievance-created \
  --property parse.key=true \
  --property key.separator=:
```

Then send this line in the producer prompt:

```text
grievance-1001:{"grievanceNo":"GRV-1001","grievanceTxnNo":"TXN-2001","email":"user@example.com","name":"Test User","createdAt":"2026-03-10T10:30:00Z"}
```

If everything is configured correctly, the service log should show event consumption and email send status.

## Build and Test

```bash
./mvnw clean test
./mvnw clean package
```

Built jar location:

```text
target/messaging-service-0.0.1-SNAPSHOT.jar
```

Run packaged jar:

```bash
java -jar target/messaging-service-0.0.1-SNAPSHOT.jar
```

## Current Limitations

- No retry or dead-letter topic handling yet.
- No REST API endpoints (event-driven service only).
- Recipient email is fixed by property unless code/config is updated.

