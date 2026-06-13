# tekravio-notification-service
tekravio-notification-service

Notification Microservice

This is a Spring Boot based notification service. It is used to send notifications through different channels like Email, SMS, WhatsApp, and Push.

It supports:

REST APIs to create and manage notifications
Kafka based async processing (partially implemented)
Strategy pattern for channel handling
Swagger UI for API testing
JWT based security (basic implementation)
Tech Stack
Java 21
Spring Boot
Spring Security (JWT)
Spring Data JPA
PostgreSQL
Redis (for caching templates)
Kafka (implemented but not fully running locally)
Swagger / OpenAPI
Features Implemented
1. Notification APIs

Implemented REST APIs:

Create notification
Get notification status
Retry failed notification
Get notification history
2. Channel Handling (Strategy Pattern)

Used strategy pattern for different channels.

Each channel has its own implementation:

Email
SMS
WhatsApp
Push Notification

A factory class is used to select the correct channel based on request.

3. Kafka (Async Design)

Kafka is added for async processing.

Flow:

API receives request
Notification should go to Kafka topic based on channel
Consumer processes message and sends notification

Note:
Kafka is implemented in code but not running in local setup. It is commented due to local setup issue.

4. Database

PostgreSQL is used.

Tables:

notifications
notification_templates
notification_recipients
notification_audit
5. Template System

Basic template system is added.

Templates stored in DB
Version support added
Template can be used for message generation
6. Security

JWT based security is added.

All APIs require token
Swagger and health endpoints are allowed without token
Role based access is added (SERVICE and ADMIN)
7. Swagger UI

Swagger is added for API testing.

URL:

http://localhost:8080/swagger-ui/index.html

It shows all APIs with request and response models.

How to Run
1. Build project
mvn clean install
2. Run application
mvn spring-boot:run
3. Open Swagger
http://localhost:8080/swagger-ui/index.html
What is NOT fully done
Kafka is not running in local (commented)
Some production features like DLQ and full retry flow are not fully tested
Docker setup is included ,but not running 
Design Choices
1. Strategy Pattern

Used to support multiple notification channels. Each channel is separate class so adding new channel is easy.

2. Kafka Async Design

Kafka is used so API does not wait for notification sending.

3. Idempotency

notificationId is used to avoid duplicate notifications.

4.redis added
for fetch template @Cacheable and @CacheEvict.

Problems faced
Kafka setup issue in local system, so it is disabled for now
Swagger dependency issue fixed during development
Security filter needed multiple changes to allow Swagger access
Future Improvements
Enable full Kafka flow with Docker setup
Add retry and DLQ handling fully
Add proper monitoring using Prometheus/Grafana
Improve template engine with better validation


Questions And Answers:
Q1: Kafka consumer crashes mid-processing

If the consumer reads a message and crashes before marking it as SENT, Kafka will not commit the offset. After restart, the same message will be read again.

So the same notification can be processed more than once.

To avoid duplicate emails, we rely on idempotency:

Each notification has a unique notificationId
Before sending email, we check in DB if it is already SENT
We update status to SENT only after successful email delivery

Still, duplicates can happen in edge cases:

Email sent successfully but DB update fails
SendGrid responds late or times out after sending email

So the system is not exactly-once, it is at-least-once with idempotency handling.

Q2: 10,000 notifications in 30 seconds, concurrency = 3

All 10,000 messages will be published to Kafka quickly.

Since only 3 threads are processing at a time:

Only 3 messages are handled in parallel
Remaining messages stay in Kafka and wait

This causes consumer lag to increase quickly.

What happens next:

Processing becomes slower as queue builds up
DB updates and SendGrid calls start getting delayed
System starts accumulating backlog

First bottleneck is not Kafka or DB directly, but external email service (SendGrid) and limited concurrency, which slows the whole pipeline.

Q3: SendGrid down for 20 minutes

During the downtime:

Consumer keeps reading messages from Kafka
Email sending fails for all requests
Messages are either retried or marked as FAILED/RETRYING based on logic

User impact:

Emails are delayed
Some emails may take time to get delivered after recovery

After SendGrid comes back:

Retry logic kicks in and messages start getting processed again
Backlog is cleared gradually

DB state during failure:

Many records will be in FAILED or RETRYING state
Eventually they move to SENT once retries succeed

Main risk here is retry overload when service comes back, which can temporarily overload SendGrid again.

Q4: Delete message content after 90 days (50M records)

Requirement is to remove message body after 90 days but keep other notification details.

Best approach:

Separate message content into a different table (notification_content)
Keep main notification table for metadata only

Migration steps:

Create new table for content
Move existing message data in batches
Update service to write to both or new structure
Stop using old column after migration

Cleanup approach:

Run a scheduled job daily
Delete records older than 90 days in small batches

Main risks:

Large delete can lock the DB
Performance issues during cleanup
Index impact and slow queries
Mistakes in job can lead to data loss

To reduce risk:

Use batch deletes instead of one big delete
Run job during low traffic
Consider table partitioning for easier deletion
Keep backup or archive if needed
