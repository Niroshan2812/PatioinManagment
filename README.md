# Patient Management System (Microservices Architecture)
A modern, robust microservices-based application built with Java and Spring Boot. This project demonstrates advanced inter-service communication patterns, showcasing both synchronous Remote Procedure Calls (via gRPC) and asynchronous event-driven messaging (via Apache Kafka).

# Architecture Overview
The system is decoupled into three primary microservices, each handling a specific business domain. It utilizes Protocol Buffers (Protobuf) as a universal serialization format to ensure highly optimized, fast, and type-safe communication across the network.

## Core Services

### Patient Service (patient_service)
- Main role is core API gateway and data management service for patient records.
- for tech stack used Spring Boot WebMVC, Spring Data JPA, PostgreSQL, H2, Kafka Producer, gRPC Client, Protobuf.
- Functionality
    - Exposes RESTful APIs (GET, POST, PUT, DELETE under /patients) for external clients.
    - Persists structured patient data to a PostgreSQL database.
    - Triggers a synchronous gRPC call to the Billing Service to open an account upon patient creation.
    - Publishes an asynchronous PatientEvent (serialized via Protobuf) to the Kafka patient topic.
 
### Billing Service (billing-service)
- Main role is Dedicated financial domain service.
- for tech stack used Spring Boot, gRPC Server (grpc-spring-boot-starter), Protobuf.
- Functionality
    - Runs a high-performance gRPC server on port 9001 (no standard HTTP REST endpoints).
    - Listens for createBillingAccount RPC requests from the Patient Service.
    - Processes the BillingRequest and synchronously returns a highly optimized binary BillingResponse (e.g., account ACTIVE, ID 12345).

### Analytics Service (analytics-service)
- Main role is Event-driven secondary service for processing metrics.
- for tech stack used Spring Boot, Spring Kafka, Protobuf.
- Functionality
    - Acts purely as a Kafka consumer listening to the patient topic.
    - Deserializes incoming binary Protobuf messages back into PatientEvent objects.
    - Executes analytics business logic in the background without impacting the main client request thread.
 
# Technology Stack & Rationale
- Java and Spring Boot
    - Enables rapid development of independent, scalable, and easily configurable microservices with embedded servers.
- PostgreSQL
    - Provides a robust, ACID-compliant relational database for reliable and structured persistence of core patient records.
- Apache Kafka
    - Facilitates asynchronous, decoupled communication. Ensures that the Patient Service is not blocked by the Analytics Service, and provides message durability if the downstream service experiences downtime.
- gRPC
    - Chosen over REST/JSON for internal, synchronous microservice communication. Operating over HTTP/2 with binary payloads, it provides an exceptionally fast, lightweight, and strongly typed network call essential for rigid transactions like billing creation.
- Protocol Buffers
    - Utilized as the serialization format for both Kafka events and gRPC payloads. It generates native Java classes from .proto files and is significantly smaller and faster to parse than JSON, reducing CPU load and network bandwidth.

# System Data Flow

When a client creates a new patient, the system executes the following flow

- Request Initiation -> The client sends a POST /patients request containing patient details to the Patient Service.
- Data Persistence -> The Patient Service securely saves the new record to the PostgreSQL database.
- Synchronous Billing Call -> The Patient Service makes an immediate, high-speed gRPC call to the Billing Service to provision a billing account. The Billing Service processes this and replies synchronously.
- Asynchronous Event Publishing -> The Patient Service serializes a "Patient Created" event into a Protobuf byte array and fires it to the Apache Kafka message broker.
- Client Response -> The Patient Service returns a successful HTTP response to the client.
- Background Processing -> The Analytics Service independently consumes the Kafka event from the broker, deserializes the data, and updates its internal metrics.

