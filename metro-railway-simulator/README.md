In Singapore, the MRT (Mass Rapid Transit) system uses contactless payment cards, such as the **EZ-Link** and **NETS FlashPay** cards, as well as contactless debit and credit cards, for fare payments. Here's how the process works:

### 1. **Tapping In: Entry**
- When entering the MRT station, passengers must tap their card on the card reader at the fare gate.
- The system records the entry point and deducts a provisional maximum fare from the card.
- The gate opens after successful card authentication.

### 2. **Tapping Out: Exit**
- Upon reaching the destination, passengers tap their card again at the fare gate when exiting.
- The system calculates the actual fare based on the distance traveled.
- The correct fare is then charged to the card, and any excess amount from the provisional fare deducted earlier is refunded to the card.

### 3. **Card and Payment Options**
- **EZ-Link / NETS FlashPay Cards**: Prepaid stored-value cards used primarily for public transport.
- **Contactless Bank Cards**: Users can also use contactless Visa, Mastercard, or mobile payment options like Apple Pay, Google Pay, etc., directly at the fare gates.
- **Account-based Ticketing (ABT)**: With ABT, the fare is deducted from a linked bank account instead of the card's stored value, allowing for fare deductions post-travel.

### 4. **Fare Calculation**
- The fare is calculated based on the distance between the entry and exit stations.
- Passengers are charged per ride according to distance traveled, with transfers between MRT and buses integrated into a single fare calculation.

This system provides a seamless and efficient way for commuters to travel on the MRT network.


Here’s a simple description for your use case:

---

### **Use Case: Real-Time MRT Fare System Simulation with Kafka Streams**

The system simulates the fare calculation process for an MRT (Mass Rapid Transit) system using Java, Spring Boot, Kafka, and Kafka Streams. When a commuter taps in at the station, the system records the entry details, and upon tapping out, it calculates the fare based on the journey.

The key features include:
- **Real-Time Event Processing**: Tap-in and tap-out events are streamed in real-time, and fares are dynamically calculated based on distance traveled.
- **Stateful Processing**: Kafka Streams retains the user’s entry data until the journey is completed (tap-out), using state stores to manage ongoing trips.
- **Concurrent Processing**: The system handles multiple commuters tapping in and out across different stations, leveraging Kafka’s partitioning for parallel processing.
- **Account Management**: User balances are tracked in real-time. If a user’s balance falls below a threshold, an alert is triggered.
- **Handling Late Events**: If a user forgets to tap out, the system applies a maximum fare using time-windowed processing.
- **Real-Time Dashboard**: A dashboard monitors commuter activity, showing metrics like the number of active commuters, fare collection, and station traffic in real-time.

This solution showcases real-time, stateful stream processing using Kafka Streams, and integrates key features like event aggregation, windowing, and real-time monitoring.

---

This description outlines the essential features while keeping it simple for general understanding.

To simulate the Singapore MRT fare system using Java, Spring Boot, threads, Kafka, and Kafka Streams (KStreams), you can design a system that captures real-time transit events (tap-in, tap-out), processes fare calculations, and manages user account states, while providing a dashboard for monitoring.

Here’s a list of **scenarios/requirements** that will cover the **KStreams Stateful Operations** and provide real-time processing with Kafka Streams:

### 1. **User Tap-in Event Processing (Stateful Stream)**:
- **Scenario**: When a user taps their card at the entry gate, create a Kafka event to record the entry station and time.
- **Requirement**: Store the entry station and timestamp in a **Kafka state store** (e.g., a KTable) keyed by the card number (or user ID).
- **Stateful Operation**: Use a **KTable** or **state store** to retain the user’s tap-in event details. The state store holds partial information (entry) until the user taps out.
- **Streams API**: Store the tap-in event and maintain user state in Kafka Streams to process the tap-out later.

### 2. **User Tap-out Event and Fare Calculation (Stateful Aggregation)**:
- **Scenario**: When a user taps out, fetch the entry details from the state store and calculate the fare based on the journey.
- **Requirement**: Use **KStream-KTable join** to join the tap-out event with the stored tap-in event.
- **Stateful Operation**: Aggregate the tap-in and tap-out events and compute the fare based on the stations.
- **Streams API**: Perform a stateful aggregation (for calculating the fare) and use **KTable join** to merge the two event streams.

### 3. **Handling Multiple Kafka Partitions (Threading)**:
- **Scenario**: Simulate multiple card tap events concurrently across different Kafka partitions.
- **Requirement**: Ensure that events from multiple users are processed in parallel using multiple Kafka partitions, with each partition handling a set of users.
- **Stateful Operation**: Kafka Streams should partition the data by user/card ID, allowing parallel processing and independent state management for each partition.
- **Streams API**: Leverage Kafka Streams threading and partition management for parallel, stateful processing.

### 4. **Tracking Real-Time User Account Balances (KTable State Store)**:
- **Scenario**: After each journey, update the user’s account balance in a **KTable**.
- **Requirement**: Maintain the account balance state in a **Kafka Streams state store** and allow users to top-up their cards via a stream of top-up events.
- **Stateful Operation**: Use **KTable** to store and update the user's balance and react to balance changes in real time.
- **Streams API**: Perform stateful updates to the account balance using `aggregate` or `reduce` operations in Kafka Streams.

### 5. **Monitoring Travel Data (Dashboard Integration with KSQL or Kafka Connect)**:
- **Scenario**: Provide a real-time dashboard to monitor the number of users traveling, current traffic between stations, and fare collection.
- **Requirement**: Use **Kafka Streams with KSQL** to aggregate and filter data (e.g., number of users between stations, fare collected) and push this to a real-time dashboard using **Kafka Connect** (for integration with monitoring systems like Prometheus/Grafana or a custom UI).
- **Stateful Operation**: Use **windowed aggregation** to compute travel statistics in real-time, such as per-hour traffic.
- **Streams API**: Use windowed operations (e.g., `count`, `sum`) to track statistics like the number of travelers in a given time window.

### 6. **Handling Late Tap-out Events (Windowing & Grace Periods)**:
- **Scenario**: If the user doesn’t tap out within a specified time (e.g., 4 hours), consider it an invalid journey and charge the maximum fare.
- **Requirement**: Use **Kafka Streams windowing** to track the time difference between tap-in and tap-out events and handle late or missing tap-outs by applying a grace period.
- **Stateful Operation**: Use **windowed joins** or time-windowed processing to handle tap-out events within a certain timeframe.
- **Streams API**: Define a **time window** for valid journeys and apply **grace periods** for delayed events.

### 7. **Error Handling and Reconciliation (Stateful Transformations)**:
- **Scenario**: If the fare calculation or account balance update fails (due to network/API failure or invalid events), store the failed events for later retry or manual intervention.
- **Requirement**: Use **stateful transformations** to handle failed records and retry the processing.
- **Stateful Operation**: Maintain a state store of failed events and retries. Track retry attempts and store error metadata.
- **Streams API**: Use `transform` or `process` to perform custom error handling and retries.

### 8. **Real-time Alerts for Low Balances (Stream-Table Join and Thresholds)**:
- **Scenario**: Notify users in real-time if their balance falls below a certain threshold when they tap out.
- **Requirement**: Stream account balances and set up a threshold alert system that triggers Kafka events or notifies a dashboard if a balance is too low.
- **Stateful Operation**: Use **KStream-KTable join** to combine tap-out events with user balances and trigger alerts if the balance is low.
- **Streams API**: Implement **filtering and branching** in Kafka Streams for alerting based on balance thresholds.

### 9. **Simulating Public Holiday Fare Adjustments (Stateful Enrichment)**:
- **Scenario**: Fares may vary on public holidays or during peak hours.
- **Requirement**: Stream in a reference dataset of holiday dates and peak hour timings and join it with the journey stream to apply dynamic fare adjustments.
- **Stateful Operation**: Use a **lookup table** or KTable for holiday data to enrich the fare calculation stream.
- **Streams API**: Use a **KTable join** to enrich the fare calculation based on additional business logic (holidays, peak hours).

### 10. **Generating Historical Reports (Stateful Aggregation and Storage)**:
- **Scenario**: Generate reports on historical travel data, such as total journeys per user or fare collection over time.
- **Requirement**: Use **stateful aggregations** to accumulate travel data over longer periods and store these for reporting.
- **Stateful Operation**: Maintain a running tally of fares and journey counts for historical reporting.
- **Streams API**: Use **windowed aggregations** to generate periodic reports and store them in a persistent sink, such as Kafka or a database.

---

### Real-Time Dashboard Implementation
For the dashboard, you can integrate Kafka with a monitoring tool:
- **Prometheus + Grafana**: Use **Kafka Connect** or **JMX Exporter** to expose real-time metrics from Kafka Streams (number of messages processed, lag, etc.).
- **Custom Web Dashboard**: Stream data to a web UI using **WebSockets** or **REST API** to display travel statistics, fare summaries, and user activity in real time.

---

### KStreams Stateful Operations Covered:
1. **KTable and State Store**: For tap-in/out data.
2. **KStream-KTable Join**: For fare calculation by combining entry and exit events.
3. **Stateful Aggregation**: For user balances and reporting.
4. **Windowing**: For managing delayed tap-out events.
5. **Error Handling with State**: For retries and reconciliation of failed transactions.
6. **Enrichment**: For dynamic fare adjustment based on holidays or peak hours.

This design ensures real-time, stateful processing using Kafka Streams and provides a rich set of scenarios for demonstrating your application’s functionality.


To simulate users tapping in and tapping out for your MRT fare system in Java using Spring Boot and Kafka, you can create a producer that generates "tap-in" and "tap-out" events. These events will be sent to Kafka topics that represent the user's actions. You can then consume these events using Kafka Streams to process the fare calculation.

Here's a step-by-step guide on how to simulate users tap in and tap out:

### 1. **Define Kafka Topics for Tap-in and Tap-out Events**
- You’ll need two Kafka topics: one for tap-in events and one for tap-out events.
- Example topics:
    - `tap-in-events`
    - `tap-out-events`

### 2. **Create Event Models**
- Define Java classes to represent the tap-in and tap-out events. These will be the payloads you send to Kafka.

   ```java
   public class TapInEvent {
       private String userId;
       private String stationId;
       private long tapInTime;

       // Constructor, getters, setters
   }

   public class TapOutEvent {
       private String userId;
       private String stationId;
       private long tapOutTime;

       // Constructor, getters, setters
   }
   ```

### 3. **Simulating User Tap-In and Tap-Out**
- Create a producer in Spring Boot that simulates users tapping in and tapping out by sending events to Kafka.

   ```java
   @RestController
   @RequestMapping("/simulate")
   public class SimulationController {

       private final KafkaTemplate<String, TapInEvent> tapInProducer;
       private final KafkaTemplate<String, TapOutEvent> tapOutProducer;

       @Autowired
       public SimulationController(KafkaTemplate<String, TapInEvent> tapInProducer,
                                   KafkaTemplate<String, TapOutEvent> tapOutProducer) {
           this.tapInProducer = tapInProducer;
           this.tapOutProducer = tapOutProducer;
       }

       @PostMapping("/tap-in")
       public String simulateTapIn(@RequestParam String userId, @RequestParam String stationId) {
           TapInEvent tapInEvent = new TapInEvent(userId, stationId, System.currentTimeMillis());
           tapInProducer.send("tap-in-events", userId, tapInEvent);
           return "User " + userId + " tapped in at station " + stationId;
       }

       @PostMapping("/tap-out")
       public String simulateTapOut(@RequestParam String userId, @RequestParam String stationId) {
           TapOutEvent tapOutEvent = new TapOutEvent(userId, stationId, System.currentTimeMillis());
           tapOutProducer.send("tap-out-events", userId, tapOutEvent);
           return "User " + userId + " tapped out at station " + stationId;
       }
   }
   ```

- **Explanation**: This code provides two endpoints to simulate user actions:
    - `POST /simulate/tap-in`: Simulates a user tapping in.
    - `POST /simulate/tap-out`: Simulates a user tapping out.

### 4. **Kafka Configuration in Spring Boot**
- Set up Kafka configuration in your Spring Boot application to produce and consume Kafka events.

**`application.yml`**:
   ```yaml
   spring:
     kafka:
       bootstrap-servers: localhost:9092
       producer:
         key-serializer: org.apache.kafka.common.serialization.StringSerializer
         value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
       consumer:
         key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
         value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
         group-id: mrt-fare-group
         properties:
           spring.json.trusted.packages: '*'
   ```

### 5. **Simulate Tap-In and Tap-Out in Bulk (Thread Simulation)**
- Create a scheduled task that simulates multiple users tapping in and out at random intervals. You can use threads or Spring’s `@Scheduled` annotation to generate events automatically.

   ```java
   @Service
   public class TapSimulationService {

       private final KafkaTemplate<String, TapInEvent> tapInProducer;
       private final KafkaTemplate<String, TapOutEvent> tapOutProducer;

       @Autowired
       public TapSimulationService(KafkaTemplate<String, TapInEvent> tapInProducer,
                                   KafkaTemplate<String, TapOutEvent> tapOutProducer) {
           this.tapInProducer = tapInProducer;
           this.tapOutProducer = tapOutProducer;
       }

       @Scheduled(fixedRate = 5000) // Simulate tap-ins every 5 seconds
       public void simulateTapInAndOut() {
           // Simulate random user and station IDs
           String userId = "user-" + (int)(Math.random() * 1000);
           String stationInId = "station-" + (int)(Math.random() * 100);
           String stationOutId = "station-" + (int)(Math.random() * 100);

           // Simulate tap-in event
           TapInEvent tapInEvent = new TapInEvent(userId, stationInId, System.currentTimeMillis());
           tapInProducer.send("tap-in-events", userId, tapInEvent);
           System.out.println("Simulated tap-in for user " + userId);

           // Delay for a few seconds before simulating tap-out
           try {
               Thread.sleep((long) (Math.random() * 10000)); // Random delay up to 10 seconds
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           // Simulate tap-out event
           TapOutEvent tapOutEvent = new TapOutEvent(userId, stationOutId, System.currentTimeMillis());
           tapOutProducer.send("tap-out-events", userId, tapOutEvent);
           System.out.println("Simulated tap-out for user " + userId);
       }
   }
   ```

- **Explanation**:
    - A scheduled task simulates a random user tapping in at a station and tapping out at another station.
    - It sends tap-in and tap-out events at random intervals.

### 6. **Kafka Streams to Process Tap-In and Tap-Out Events**
- Use Kafka Streams to process the events and compute the fare between stations.

**Kafka Streams Example**:
   ```java
   @Component
   public class FareProcessor {

       @Autowired
       public void process(StreamsBuilder streamsBuilder) {
           // Stream tap-in events
           KStream<String, TapInEvent> tapInStream = streamsBuilder.stream("tap-in-events");

           // Stream tap-out events
           KStream<String, TapOutEvent> tapOutStream = streamsBuilder.stream("tap-out-events");

           // Join tap-in and tap-out streams on user ID and calculate fare
           KStream<String, FareEvent> fareStream = tapInStream
                   .join(tapOutStream,
                         this::calculateFare, // Fare calculation logic
                         JoinWindows.of(Duration.ofMinutes(120)), // 2-hour window to pair tap-in/out
                         Joined.with(Serdes.String(), new JsonSerde<>(TapInEvent.class), new JsonSerde<>(TapOutEvent.class)));

           // Output fare events to a new topic for further processing
           fareStream.to("fare-events", Produced.with(Serdes.String(), new JsonSerde<>(FareEvent.class)));
       }

       private FareEvent calculateFare(TapInEvent tapInEvent, TapOutEvent tapOutEvent) {
           // Calculate fare based on station IDs and time traveled
           double fare = computeFare(tapInEvent.getStationId(), tapOutEvent.getStationId());
           return new FareEvent(tapInEvent.getUserId(), fare, tapInEvent.getStationId(), tapOutEvent.getStationId());
       }

       private double computeFare(String stationIn, String stationOut) {
           // Fare calculation logic (e.g., distance-based pricing)
           return Math.random() * 5; // Dummy fare calculation
       }
   }
   ```

- **Explanation**:
    - The system joins the tap-in and tap-out events for the same user within a 2-hour window and computes the fare.
    - The result (a `FareEvent`) is sent to a new Kafka topic (`fare-events`) for further processing or dashboard visualization.

### 7. **Dashboard and Real-Time Monitoring**
- You can consume the `fare-events` topic to show real-time data on a dashboard (e.g., using WebSocket or REST APIs).
- Example data to display:
    - Number of users currently traveling.
    - Fares calculated per minute.
    - User balances or low-balance alerts.

### Summary
- **Tap-in and Tap-out Simulation**: You can simulate tap-in and tap-out events using a Spring Boot producer that generates events and sends them to Kafka topics.
- **Kafka Streams Processing**: Use Kafka Streams to join the tap-in and tap-out events, calculate fares, and process the user journey.
- **Real-Time Dashboard**: Consume the processed fare events to provide real-time monitoring on a dashboard.

This approach allows for an end-to-end simulation of an MRT fare system using Kafka, with real-time processing and stateful operations.


To enhance your MRT fare simulation use case by incorporating an **Event Store** to store all user events and provide an endpoint to fetch each user's travel history, here’s how the updated requirements would look:

---

### **Enhanced Requirements for MRT Fare Simulation with Event Store**

#### 1. **Event-Driven System with Event Store**
- **Event Store**: Implement an event store to persist all events related to a user’s journey, including:
    - `TapInEvent`: When the user taps into a station.
    - `TapOutEvent`: When the user taps out of a station.
    - `FareCalculatedEvent`: When the fare is calculated based on the tap-in and tap-out events.

- The event store should capture the following information for each event:
    - **User ID**
    - **Station Details** (station ID, name, etc.)
    - **Event Type** (`TapIn`, `TapOut`, `FareCalculated`)
    - **Timestamp** of the event
    - **Metadata** (device used for tap, special conditions like fare adjustments)

#### 2. **Kafka Streams Integration with Event Store**
- The Kafka Streams application will:
    - **Stream tap-in and tap-out events** from Kafka topics.
    - **Store all events** in the event store (e.g., MongoDB, DynamoDB, PostgreSQL, or a specialized event store like EventStoreDB) in a time-ordered fashion.
    - **Track user state** (active journey or completed journey) using a state store in Kafka Streams to correlate tap-in and tap-out events.
    - **Generate a FareCalculatedEvent** once the user completes the journey (tap-out), and store it in the event store.

#### 3. **User History API (Event Sourcing)**
- Provide an API to retrieve the **event history** for any user. The API will:
    - Return all events related to a particular user in the order they occurred.
    - Provide filters to fetch events for a specific date range or for specific types of events (e.g., only fare calculation events).

**Sample Endpoint**:
   ```http
   GET /user/{userId}/events?from={startDate}&to={endDate}&eventType={type}
   ```
- **Response Example**:
  ```json
  [
    {
      "userId": "user-123",
      "eventType": "TapIn",
      "stationId": "station-1",
      "timestamp": 1695900000000,
      "metadata": { "device": "Gate-A", "fareAdjustment": false }
    },
    {
      "userId": "user-123",
      "eventType": "TapOut",
      "stationId": "station-5",
      "timestamp": 1695909000000,
      "metadata": { "device": "Gate-B", "fareAdjustment": false }
    },
    {
      "userId": "user-123",
      "eventType": "FareCalculated",
      "fare": 3.50,
      "tapInStation": "station-1",
      "tapOutStation": "station-5",
      "timestamp": 1695910000000,
      "metadata": { "fareType": "Regular", "adjustments": [] }
    }
  ]
  ```

#### 4. **Additional Event Store Scenarios**

1. **User Event Replay (Event Sourcing)**:
    - **Replay user events**: If there is a failure or error in calculating a user’s fare, the system should be able to **replay the events** (tap-in, tap-out) for that user from the event store and re-calculate the fare.
    - If the fare calculation failed, an admin should be able to trigger a reprocessing of that user's journey using the stored events.

2. **Handling Late Events**:
    - Late tap-out events (e.g., if a user forgets to tap out) should be **time-windowed** using Kafka Streams. If a tap-out event is not received within a certain window, a default fare will be applied, but the event store should **keep track of both the late tap-out event** and the **default fare** applied initially.
    - The event store will capture both events:
        - A `DefaultFareAppliedEvent` if no tap-out is received within the window.
        - A `LateTapOutEvent` if the user taps out late.

3. **Event Store Versioning and Snapshots**:
    - **Versioning**: Each event will have a version to ensure immutability, and if there are updates (e.g., fare adjustment), the event store should keep **multiple versions** of the event.
    - **Snapshots**: To improve performance for users with a large event history, the system should support **snapshots** that aggregate events up to a certain point (e.g., after the user taps out, a snapshot of their journey can be taken).

4. **Event Compensation (Correcting Mistakes)**:
    - If a fare is incorrectly calculated (e.g., due to wrong station info), an `EventCompensation` process will be used to adjust the fare. The compensation will be stored as a new event (`FareAdjustmentEvent`), without deleting the original `FareCalculatedEvent`.
    - The event store will store **compensating events** to handle any necessary corrections (without modifying existing events).

5. **Real-Time Event Streaming for Monitoring**:
    - The system should support **streaming of events** (in real-time) to an external service or dashboard for monitoring and analytics. This can include real-time traffic at stations, fare trends, and alerts for anomalies (e.g., too many tap-outs at a single station in a short time).
    - The event store will be integrated with a dashboard that shows live data of user journeys.

#### 5. **Fare Adjustment Scenarios**
- The system should support **fare adjustments** (e.g., applying discounts for seniors, special conditions, or promotional fares).
- These adjustments will be stored in the event store as **FareAdjustmentEvent**, capturing:
    - Reason for adjustment (e.g., senior citizen, promotional fare).
    - Adjusted fare value.
    - Metadata like the user’s profile and station details.

#### 6. **Concurrency and Fault Tolerance**
- Ensure that the event store supports **concurrent writes and reads** to handle multiple users tapping in and out at different stations.
- **Kafka Streams** and the event store should be configured to handle **fault tolerance**. In case of failures (network or service downtime), events should be persisted once the system is back online.
- **Exactly-once semantics**: Ensure that Kafka Streams processes events exactly once to avoid duplicating user events or fare calculations.

#### 7. **End-of-Day Processing and Summary Events**
- At the end of each day, a **summary event** should be generated for each user, containing aggregated data like the total fare for the day, total trips, and any applicable discounts.
- This summary can be part of the event store and queried as part of the user history.

---

### **Architecture Overview**

1. **Event Generation (Tap-In, Tap-Out Events)**:
    - Users tap in and out of stations. Events are generated via REST API and sent to Kafka topics (`tap-in-events`, `tap-out-events`).

2. **Kafka Streams (Event Processing and Fare Calculation)**:
    - Kafka Streams processes the tap-in and tap-out events.
    - The stream stores the state (ongoing trips) and calculates the fare when a user completes a journey.

3. **Event Store (Historical Data and Event Replay)**:
    - The event store stores all events (tap-in, tap-out, fare calculated) in a time-ordered sequence.
    - The system allows querying the event store to retrieve the full event history for any user.

4. **Event Replay and Compensation**:
    - In case of failure or fare miscalculation, events can be replayed from the event store to recalculate fares.
    - Event compensation can be applied by creating new events to adjust errors.

5. **User History API**:
    - A REST API allows fetching a user’s travel history by querying the event store.

6. **Real-Time Monitoring**:
    - Events are streamed to a real-time dashboard, showing current system metrics like the number of active commuters, station traffic, and fare collection.

---

### **Example Flow**:

1. **Tap-in**:
    - User taps in at station A.
    - `TapInEvent` is generated and sent to Kafka.
    - Kafka Streams processes the event and stores it in the event store.

2. **Tap-out**:
    - User taps out at station B.
    - `TapOutEvent` is generated and sent to Kafka.
    - Kafka Streams joins the tap-in and tap-out events and calculates the fare.
    - `FareCalculatedEvent` is generated and stored in the event store.

3. **User History**:
    - The user’s full journey history (tap-in, tap-out, fare calculated) can be retrieved via the `/user/{userId}/events` endpoint.

---

This enhanced system adds the capability to store, query, and replay events, enabling event sourcing and auditability while maintaining the core MRT fare simulation functionality.


To simulate multiple users tapping in and out in a real-time simulation, you can use **multi-threading** in Java and generate random events that mimic users tapping in and out at various MRT stations. You can publish these events to Kafka topics (e.g., `tap-in-events`, `tap-out-events`), process them with **Kafka Streams**, and store them in an event store.

Here’s a step-by-step guide to simulate the tap-in and tap-out process for multiple users:

### **1. Simulate Tap-in and Tap-out Events with Java and Spring Boot**

We'll create a simple Java class that simulates users tapping in and out of MRT stations, publishing those events to Kafka in real-time.

#### **Components Involved:**
- **Kafka**: To publish tap-in and tap-out events to corresponding topics.
- **Spring Boot Scheduler**: To simulate users tapping in and out.
- **Threads**: To simulate multiple users performing actions concurrently.
- **Randomization**: To randomize user actions and station details.

### **2. Sample Tap-in and Tap-out Event Structure**

Create simple event models for `TapInEvent` and `TapOutEvent`:

```java
public class TapInEvent {
    private String userId;
    private String stationId;
    private LocalDateTime tapInTime;
    // Constructors, getters, and setters
}

public class TapOutEvent {
    private String userId;
    private String stationId;
    private LocalDateTime tapOutTime;
    // Constructors, getters, and setters
}
```

### **3. Kafka Configuration (Spring Boot)**

In your Spring Boot application, configure Kafka to produce messages:

```yaml
# application.yml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### **4. User Simulation with Multi-threading**

We can simulate multiple users tapping in and out of MRT stations using Java's `ExecutorService` to manage concurrent threads.

Here’s a Java class that simulates random user actions:

```java
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MRTSimulationService {

    private final KafkaTemplate<String, TapInEvent> tapInKafkaTemplate;
    private final KafkaTemplate<String, TapOutEvent> tapOutKafkaTemplate;
    
    private static final String TAP_IN_TOPIC = "tap-in-events";
    private static final String TAP_OUT_TOPIC = "tap-out-events";
    private static final String[] STATION_IDS = {"Station-A", "Station-B", "Station-C", "Station-D"};

    public MRTSimulationService(KafkaTemplate<String, TapInEvent> tapInKafkaTemplate,
                                KafkaTemplate<String, TapOutEvent> tapOutKafkaTemplate) {
        this.tapInKafkaTemplate = tapInKafkaTemplate;
        this.tapOutKafkaTemplate = tapOutKafkaTemplate;
    }

    @Scheduled(fixedRate = 1000) // Schedule every second
    public void simulateUsersTapInOut() {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 concurrent users

        for (int i = 0; i < 10; i++) {
            final String userId = "user-" + i;
            executorService.submit(() -> {
                simulateUserAction(userId);
            });
        }
        executorService.shutdown();
    }

    private void simulateUserAction(String userId) {
        Random random = new Random();
        String stationId = STATION_IDS[random.nextInt(STATION_IDS.length)];
        
        // Simulate Tap In Event
        TapInEvent tapInEvent = new TapInEvent(userId, stationId, LocalDateTime.now());
        tapInKafkaTemplate.send(TAP_IN_TOPIC, tapInEvent);
        System.out.println("User " + userId + " tapped in at " + stationId);

        // Simulate delay for Tap Out (random delay between 1 to 5 seconds)
        try {
            Thread.sleep((1 + random.nextInt(5)) * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate Tap Out Event
        TapOutEvent tapOutEvent = new TapOutEvent(userId, STATION_IDS[random.nextInt(STATION_IDS.length)], LocalDateTime.now());
        tapOutKafkaTemplate.send(TAP_OUT_TOPIC, tapOutEvent);
        System.out.println("User " + userId + " tapped out at " + tapOutEvent.getStationId());
    }
}
```

#### **Explanation:**
1. **Scheduled User Action**:
    - A `@Scheduled` method runs every second, simulating 10 users tapping in and out using a thread pool (`ExecutorService`).
    - Each user is assigned a thread, which simulates a tap-in at a random station and then a tap-out after a random delay.

2. **Randomization**:
    - The user taps in at a random station from a predefined list (`STATION_IDS`).
    - After a short delay, the user taps out at another random station.

3. **Kafka Integration**:
    - Tap-in and tap-out events are published to respective Kafka topics (`tap-in-events`, `tap-out-events`).

### **5. Kafka Streams for Processing Events**

Once the tap-in and tap-out events are generated and sent to Kafka, you can process them using **Kafka Streams** for business logic, such as fare calculation or storing them in an event store.

Here’s a basic setup for Kafka Streams to process tap-in and tap-out events:

```java
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "mrt-fare-simulation");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, TapInEvent> kStream(StreamsBuilder builder) {
        KStream<String, TapInEvent> tapInStream = builder.stream("tap-in-events");
        KStream<String, TapOutEvent> tapOutStream = builder.stream("tap-out-events");

        // Join streams, calculate fare, and output to another Kafka topic
        KStream<String, FareCalculatedEvent> fareStream = tapInStream.join(
                tapOutStream,
                (tapIn, tapOut) -> calculateFare(tapIn, tapOut),
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(1)),
                Serdes.String(),
                new SpecificAvroSerde<>(),
                new SpecificAvroSerde<>()
        );

        fareStream.to("fare-calculated-events");

        return tapInStream;
    }

    private FareCalculatedEvent calculateFare(TapInEvent tapIn, TapOutEvent tapOut) {
        // Implement fare calculation logic here
        double fare = Math.random() * 5;  // Simulated fare
        return new FareCalculatedEvent(tapIn.getUserId(), fare, tapIn.getStationId(), tapOut.getStationId());
    }
}
```

### **6. Simulating Real-Time User History Dashboard**

You can connect this real-time event processing with a **dashboard** (e.g., via WebSockets or any monitoring tool) to visualize ongoing trips, total number of users in transit, fares calculated, and more.

- **WebSocket Integration**: Send real-time updates to a UI for each new event processed.
- **Kafka Stream Sink**: Stream real-time events (e.g., fare calculations) to the UI for monitoring.

### **7. Event Store and User History API**

You would also persist these tap-in, tap-out, and fare calculation events in an **event store** like MongoDB or PostgreSQL. You can then build a REST API to retrieve the full history of events for each user:

```java
@RestController
@RequestMapping("/user/{userId}/history")
public class UserHistoryController {

    @Autowired
    private UserEventStoreService userEventStoreService;  // Service to fetch user events from event store

    @GetMapping
    public List<UserEvent> getUserEventHistory(@PathVariable String userId) {
        return userEventStoreService.getUserHistory(userId);
    }
}
```

### **Conclusion**

By following this approach, you can simulate multiple users tapping in and out of MRT stations in real-time using Java, Spring Boot, Kafka, and Kafka Streams. The system will produce a large number of events to simulate real-world scenarios, and Kafka Streams will process these events for downstream services like fare calculation and event storage.

This setup can easily be extended to handle other functionalities such as failure handling, retries, and integrating with a dashboard for real-time monitoring.

Here are the **final requirements** for implementing the simulation of multiple users tapping in and out of MRT stations using **Java, Spring Boot, Kafka, Kafka Streams, Event Store**, and other components:

---

## **Functional Requirements:**

### **1. Tap-in and Tap-out Simulation**
- Simulate multiple users tapping in and out at MRT stations.
- Each user performs a `tap-in` when entering a station and a `tap-out` when exiting.
- User actions should be randomized across different stations and users.
- User actions will be generated using multi-threading to simulate concurrent activity.
- Events (`TapInEvent` and `TapOutEvent`) will be published to Kafka topics in real-time.

### **2. Real-Time Processing with Kafka Streams**
- Consume `tap-in` and `tap-out` events from respective Kafka topics (`tap-in-events` and `tap-out-events`).
- Join the `tap-in` and `tap-out` events to calculate fare for each user trip.
    - The fare calculation logic should consider the distance between stations.
- Publish fare calculation results to a new Kafka topic (`fare-calculated-events`).

### **3. Event Store for User History**
- All `tap-in`, `tap-out`, and `fare-calculated` events should be stored in an event store (e.g., MongoDB, PostgreSQL).
- Create an API to retrieve the full event history for each user:
    - `/user/{userId}/history`: Fetch all tap-in, tap-out, and fare calculation events for the given user.

### **4. Retry and Failure Handling**
- Implement failure handling and retry mechanisms in Kafka Streams for event processing.
    - If fare calculation fails due to any reason, retry a configurable number of times.
    - Store the status of each job (e.g., completed, failed) in the event store for auditing purposes.
- Use reconciliation logic to process any missed or failed events.

### **5. Dashboard for Real-Time Monitoring**
- Implement a real-time dashboard to monitor:
    - Number of active users (users who have tapped in but not yet tapped out).
    - Fare calculation results as they happen in real-time.
    - The status of failed jobs and retries.
- Use WebSocket or a similar real-time communication protocol to stream events to the dashboard.

---

## **Non-Functional Requirements:**

### **1. Scalability**
- The system should scale to handle a large number of users tapping in and out concurrently.
- Kafka and Kafka Streams should be configured to handle high throughput and large message volumes.

### **2. Fault Tolerance**
- The system should gracefully handle failures, such as Kafka topic unavailability or processing failures in Kafka Streams.
- Implement mechanisms to handle retries and ensure the event store remains consistent.

### **3. Performance**
- Ensure that tap-in and tap-out events are processed with low latency, and fare calculations happen in real-time.
- The system should process thousands of concurrent users tapping in and out without degradation in performance.

---

## **Detailed Requirements:**

### **1. Event Models**
- **TapInEvent**: User taps in at an MRT station.
    - Fields: `userId`, `stationId`, `tapInTime`
- **TapOutEvent**: User taps out at an MRT station.
    - Fields: `userId`, `stationId`, `tapOutTime`
- **FareCalculatedEvent**: Fare calculated for a user trip.
    - Fields: `userId`, `fareAmount`, `startStation`, `endStation`, `tapInTime`, `tapOutTime`

### **2. Kafka Topics**
- **tap-in-events**: Publish `TapInEvent` when users tap in at a station.
- **tap-out-events**: Publish `TapOutEvent` when users tap out of a station.
- **fare-calculated-events**: Publish the results of fare calculations.

### **3. Kafka Streams Processing**
- Use Kafka Streams to:
    - Consume `tap-in-events` and `tap-out-events`.
    - Join events based on `userId` to calculate fare.
    - Publish the fare calculation to the `fare-calculated-events` topic.
    - Ensure all stateful operations, like windowing and joins, are handled in Kafka Streams.

### **4. Event Store**
- Store all events (`TapInEvent`, `TapOutEvent`, `FareCalculatedEvent`) in the event store.
- Each user’s history should be queryable by `userId`.
    - `/user/{userId}/history`: Fetch all events related to a specific user.

### **5. Real-Time User Simulation**
- Simulate concurrent user tap-in and tap-out actions:
    - Use multi-threading to simulate hundreds or thousands of users.
    - Each user should tap in and tap out at random intervals, using random stations.
- Implement random delays between tap-in and tap-out to simulate real-world scenarios.

### **6. APIs**
- **User History API**: Expose an endpoint to retrieve a user’s full tap-in, tap-out, and fare history.
    - `/user/{userId}/history`: Returns a list of all events for the user.

### **7. Retry Mechanism and Reconciliation**
- Implement retry logic for failed events in Kafka Streams (e.g., fare calculation failures).
- If retry fails, trigger a reconciliation process to handle incomplete jobs and notify administrators.
- Store reconciliation job status (e.g., completed, failed) in the event store for tracking.

### **8. Dashboard**
- Build a real-time dashboard to monitor the state of the system:
    - Display active users currently in transit.
    - Show the latest fare calculations.
    - Monitor failed events and jobs requiring retries.
- Stream real-time data to the dashboard using WebSockets or a similar mechanism.

---

## **Technologies and Tools:**
- **Java, Spring Boot**: Core backend framework for building microservices.
- **Kafka**: For real-time event streaming and processing.
- **Kafka Streams (KStreams)**: To process and join `tap-in` and `tap-out` events, and for fare calculation.
- **Event Store (MongoDB/PostgreSQL)**: To store all user events.
- **Multi-threading/ExecutorService**: To simulate concurrent user actions.
- **WebSockets**: To stream real-time data to the dashboard.

---

## **Additional Scenarios (Event Store Considerations):**

1. **User Tap-in Without Tap-out**
    - If a user taps in but does not tap out after a certain period, mark the trip as incomplete.
    - Notify administrators for reconciliation and investigation.

2. **Historical Data Fetching**
    - The event store should allow fetching historical data for users, including trips spanning days, weeks, or months.
    - Support for pagination and filtering by date/time.

3. **Auditing and Reporting**
    - Maintain a log of all system activities (tap-in, tap-out, fare calculations, failures) for auditing.
    - Provide API endpoints for generating reports on total trips, total revenue, and failed transactions over a specified period.

---

### **Summary:**
This project involves building a real-time simulation for MRT tap-in and tap-out activities, processing the events with Kafka Streams, storing the data in an event store, and providing APIs and a dashboard to monitor the system in real-time. The system should handle large-scale user traffic, ensure fault tolerance, and provide mechanisms for retry and reconciliation in case of failures.

To use **EventStoreDB** instead of PostgreSQL for storing all user events (tap-in, tap-out, fare-calculated) in your simulation project, follow these steps:

---

### **1. Set up EventStoreDB**
First, you'll need to set up an instance of EventStoreDB.

#### **Local Setup with Docker:**
To run EventStoreDB locally using Docker, you can use the following command:

```bash
docker run --name eventstore -d -p 2113:2113 -p 1113:1113 \
    eventstore/eventstore:latest --insecure --run-projections=All
```

This will:
- Run EventStoreDB on port `2113` (HTTP API and UI) and `1113` (TCP communication).
- Enable projections to allow stream processing.
- Allow insecure connections for local development.

You can access the EventStoreDB admin UI at: `http://localhost:2113/`

---

### **2. Add EventStoreDB Client to Spring Boot**

#### **Maven Dependency:**

Add the EventStoreDB client dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.eventstore.dbclient</groupId>
    <artifactId>eventstore-client-java</artifactId>
    <version>3.0.1</version>
</dependency>
```

#### **Configuration in `application.yml`:**

Configure your EventStoreDB connection settings in your Spring Boot `application.yml`:

```yaml
eventstoredb:
  connectionString: "esdb://localhost:2113?tls=false" # Change to your connection URL
  username: "admin" # Optional if you have authentication enabled
  password: "changeit"
```

---

### **3. Define Event Models**

Define your event classes for `TapInEvent`, `TapOutEvent`, and `FareCalculatedEvent`. Here’s an example:

```java
public class TapInEvent {
    private String userId;
    private String stationId;
    private Instant tapInTime;

    // Getters, setters, and constructors
}

public class TapOutEvent {
    private String userId;
    private String stationId;
    private Instant tapOutTime;

    // Getters, setters, and constructors
}

public class FareCalculatedEvent {
    private String userId;
    private double fareAmount;
    private String startStation;
    private String endStation;
    private Instant tapInTime;
    private Instant tapOutTime;

    // Getters, setters, and constructors
}
```

---

### **4. EventStoreDB Repository Layer**

Create a repository class to handle event persistence using EventStoreDB:

```java
import com.eventstore.dbclient.*;
import org.springframework.stereotype.Service;

@Service
public class EventStoreRepository {

    private final EventStoreDBClient eventStoreDBClient;

    public EventStoreRepository() {
        // Connect using connection string
        this.eventStoreDBClient = EventStoreDBClient.create("esdb://localhost:2113?tls=false");
    }

    // Append events to EventStore
    public void appendEvent(String streamId, Object event) throws Exception {
        // Convert your event object to JSON
        String eventData = new ObjectMapper().writeValueAsString(event);
        EventData eventPayload = EventData.builderAsJson(event.getClass().getSimpleName(), eventData).build();

        // Append the event to a stream (streamId is unique to each user or trip)
        eventStoreDBClient.appendToStream(streamId, eventPayload).get();
    }

    // Read all events for a stream (userId)
    public List<ResolvedEvent> getUserEvents(String streamId) throws Exception {
        ReadStreamOptions options = ReadStreamOptions.get().forwards().fromStart();
        return eventStoreDBClient.readStream(streamId, options).get().getEvents();
    }
}
```

In the code above:
- `appendEvent`: Writes an event to a specific stream in EventStoreDB. Each user's events can be appended to their unique stream (`streamId` can be their `userId` or a trip identifier).
- `getUserEvents`: Retrieves all events for a specific user.

---

### **5. Storing User Events**

Whenever a user taps in or taps out, or when fare is calculated, append the event to EventStoreDB:

#### Example of storing a tap-in event:

```java
public class TapService {

    private final EventStoreRepository eventStoreRepository;

    public TapService(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    public void tapIn(String userId, String stationId) {
        TapInEvent event = new TapInEvent(userId, stationId, Instant.now());
        try {
            eventStoreRepository.appendEvent(userId, event); // Stream identified by userId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tapOut(String userId, String stationId) {
        TapOutEvent event = new TapOutEvent(userId, stationId, Instant.now());
        try {
            eventStoreRepository.appendEvent(userId, event); // Stream identified by userId
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Each user will have their own stream of events (e.g., `userId` is used as the stream identifier).

---

### **6. Retrieving User History**

You can expose an API to retrieve a user's event history from EventStoreDB:

```java
@RestController
@RequestMapping("/users")
public class UserController {

    private final EventStoreRepository eventStoreRepository;

    public UserController(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @GetMapping("/{userId}/history")
    public List<Object> getUserHistory(@PathVariable String userId) {
        try {
            List<ResolvedEvent> events = eventStoreRepository.getUserEvents(userId);
            return events.stream()
                         .map(resolvedEvent -> resolvedEvent.getOriginalEvent().getEventData())
                         .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user history", e);
        }
    }
}
```

This API fetches all events for a given `userId` and returns them to the client as JSON.

---

### **7. Additional Scenarios with EventStoreDB**

With EventStoreDB, you can handle additional scenarios such as:
1. **Event Replay**: Replay all events for a given stream (e.g., for auditing or data recovery).
2. **Projections**: Create projections in EventStoreDB to aggregate and filter events in real-time (e.g., to track active users in transit).
3. **Concurrency Control**: EventStoreDB can handle optimistic concurrency, ensuring no two events are written simultaneously to the same stream.
4. **Idempotency**: Ensure that duplicate events (due to retries) are not written multiple times to a stream by checking event IDs.

---

### **Conclusion:**

To implement your real-time tap-in/tap-out simulation with **EventStoreDB**, you'll:
- Set up EventStoreDB.
- Use Kafka to simulate and publish events.
- Use EventStoreDB to persist all events (tap-in, tap-out, fare calculation).
- Create API endpoints to query user histories and events.
- Leverage EventStoreDB’s projections and streaming capabilities for more advanced event processing and analytics.

This approach will give you a scalable, event-driven architecture suited for real-time scenarios.


Deciding train fares for your metro rail simulator program involves several key considerations, ranging from operational costs to user experience. Here’s a step-by-step guide to help you develop a fare structure that balances realism, complexity, and user engagement:

### 1. **Define the Key Factors**
These factors can vary based on the real-world metro systems you want to simulate or base your program on:

- **Distance Traveled**: Most metro systems charge based on how far a passenger travels.
- **Time of Travel**: Peak vs. off-peak hours might have different fares.
- **Passenger Type**: Different fares for adults, students, seniors, etc.
- **Zonal or Sectional Fares**: Fares divided by city zones or fixed sections.
- **Ticket Type**: Single journey, return tickets, daily/weekly passes.
- **Service Class**: Regular vs. premium (if applicable).

### 2. **Fare Models to Consider**
You can choose from several fare structures or combine them:

- **Flat Fare**: Passengers pay the same amount no matter how far they travel.
    - **Example**: Some cities like New York Metro or Delhi Metro use flat fares for certain distances or sections.

- **Distance-Based Fare**: Fare increases with the distance traveled.
    - **Example**: Most systems like London Underground use a fare-per-kilometer or fare-per-mile system.

- **Zonal Fare**: The metro area is divided into zones, and fares increase based on the number of zones crossed during the journey.
    - **Example**: Paris, London Underground, and Berlin U-Bahn have zonal fare systems.

- **Time-Based Fare**: Travel within a specific time period costs a certain amount. Some systems charge more during peak times.
    - **Example**: Tokyo Metro charges more during rush hours.

- **Subscription Fare**: Monthly or weekly passes for regular commuters, often at discounted rates.

### 3. **Steps to Implement Fares**
Depending on the fare structure you choose, you can break it down into the following steps:

- **Distance-Based Fare Calculation**:
    - Set a **base fare** for a minimum distance.
    - Define a **rate per additional unit of distance** (e.g., per kilometer or per mile).
    - Implement a formula:
      \[
      \text{Fare} = \text{Base Fare} + (\text{Distance Traveled} \times \text{Rate per km})
      \]

- **Zonal Fare Calculation**:
    - Divide your city map into zones.
    - Charge based on the number of zones crossed, which could be:
      \[
      \text{Fare} = \text{Base Fare for Zone 1} + (\text{Zones Crossed} \times \text{Rate per Zone})
      \]

- **Time-Based Fare**:
    - Set different fares for peak and off-peak hours.
    - You can have a static rate during certain time windows or dynamically change the fare based on system load (rush hour).

- **Passenger Type Discounts**:
    - Assign discounts for various types of passengers:
        - Students: 10-30% off
        - Seniors: 25-50% off
        - Children: Often travel free or at a nominal cost.
    - Implement this as:
      \[
      \text{Fare} = \text{Base Fare} \times \text{Discount Rate for Type}
      \]

- **Ticket Types**:
    - **Single journey**: Full fare for one trip.
    - **Return ticket**: Slight discount on buying a round trip.
    - **Passes**: Implement weekly/monthly passes that give unlimited rides for a flat rate.

### 4. **Realistic Considerations for Your Simulator**
To create a more realistic experience:
- **Inflation and Operational Costs**: You can introduce fare hikes over time in your simulator to simulate inflation or rising operational costs.
- **Dynamic Pricing**: You could make fares adjust based on crowd levels (e.g., higher prices during high demand).
- **Fare Capping**: Some systems cap the total fare for a day or a week, so regular commuters don’t pay over a certain amount.
- **Promotional Pricing**: For example, the first week of opening a new metro line could offer free or reduced fares.

### 5. **User Experience in the Simulator**
- Provide fare calculators or tools so users can check how much a journey will cost in advance.
- Display the breakdown of fares (e.g., base fare, zone crossed, and any discounts applied).
- Allow the simulation to display various payment methods (smart cards, tickets, contactless payments).

### 6. **Testing and Balancing**
- Run simulations using different fare structures and see how they affect the balance between system income and commuter satisfaction.
- Ensure that the fares are neither too high (discouraging travel) nor too low (leading to unprofitable operations).

### 7. **Optional Enhancements**:
- **Real-Time Fare Adjustments**: Fares could change based on network load or service disruptions.
- **Peak Travel Pricing**: Higher fares during rush hours and lower fares during off-peak times.
- **Variable Class Tiers**: Offer multiple service tiers (e.g., normal and express) with different pricing.

By incorporating these steps, you can build a comprehensive and dynamic fare system into your metro rail simulator that is realistic, flexible, and enhances the simulation experience for users.
To store the various conditions and rules for calculating metro train fares in a database, you'll need to design a schema that can handle multiple fare structures, passenger types, zones, and other parameters.

Here’s a database schema outline you could consider for storing the fare conditions:

### 1. **Tables Overview**
- **Distance-Based Fares**
- **Zonal Fares**
- **Time-Based Fares (Peak/Off-Peak)**
- **Passenger Types and Discounts**
- **Ticket Types**
- **Fare Capping**
- **Fare Logs** (for auditing fare calculations)

### 2. **Database Tables and Their Structure**

#### **1. Distance-Based Fare Table**

This table stores the base fare and the rate per kilometer/mile.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the rule         |
| `base_fare`       | DECIMAL   | The minimum fare for the journey            |
| `rate_per_km`     | DECIMAL   | Additional fare per kilometer               |
| `min_distance`    | DECIMAL   | Minimum distance covered by the base fare   |
| `max_distance`    | DECIMAL   | Maximum distance covered by the base fare   |
| `currency`        | VARCHAR   | Currency for the fare (e.g., USD, EUR)      |

#### **2. Zonal Fare Table**

This table tracks fare rules based on zones crossed.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the rule         |
| `zone_from`       | INT       | Zone from which the passenger departs       |
| `zone_to`         | INT       | Destination zone                            |
| `fare`            | DECIMAL   | Fare for traveling between these zones      |
| `currency`        | VARCHAR   | Currency (e.g., USD, EUR)                   |

#### **3. Time-Based Fare Table (Peak/Off-Peak)**

For storing different fares for peak and off-peak times.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the rule         |
| `time_from`       | TIME      | Start of time range (e.g., 07:00 for peak)  |
| `time_to`         | TIME      | End of time range (e.g., 09:00 for peak)    |
| `fare_multiplier` | DECIMAL   | Multiplier for fares during this time       |
| `peak`            | BOOLEAN   | TRUE if this is a peak-time fare            |

#### **4. Passenger Type Table (Discounts)**

This table defines various passenger categories and their respective discounts.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the passenger type |
| `type`            | VARCHAR   | Type of passenger (e.g., adult, student)    |
| `discount_rate`   | DECIMAL   | Discount percentage (e.g., 0.20 for 20%)    |

#### **5. Ticket Type Table**

This table manages different types of tickets (single, return, pass, etc.).

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the ticket type  |
| `ticket_type`     | VARCHAR   | Ticket type (e.g., single, return, monthly) |
| `fare_multiplier` | DECIMAL   | Multiplier for this type of ticket (e.g., 1.8 for return) |

#### **6. Fare Capping Table**

This table defines limits for daily/weekly fare caps.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `id`              | INT       | Primary key, unique ID for the rule         |
| `cap_type`        | VARCHAR   | Type of cap (e.g., daily, weekly)           |
| `cap_value`       | DECIMAL   | Maximum fare amount in the specified period |
| `period_duration` | VARCHAR   | Duration of the cap (e.g., day, week)       |

#### **7. Fare Log Table**

This table stores the fare calculations for each trip to maintain a history or audit trail.

| Column            | Data Type | Description                                 |
|-------------------|-----------|---------------------------------------------|
| `log_id`          | INT       | Primary key, unique log ID                  |
| `user_id`         | INT       | User identifier                             |
| `fare`            | DECIMAL   | Final fare calculated                       |
| `distance`        | DECIMAL   | Distance traveled                           |
| `zone_from`       | INT       | Start zone                                  |
| `zone_to`         | INT       | End zone                                    |
| `ticket_type`     | VARCHAR   | Ticket type used (single, return, pass)     |
| `passenger_type`  | VARCHAR   | Passenger type (adult, student, etc.)       |
| `travel_time`     | DATETIME  | Timestamp for the journey                   |
| `fare_conditions` | TEXT      | JSON data containing fare breakdown         |

### 3. **Schema Relationships**
- **Distance-Based Fare**: Linked to the journey (distance and zone crossed).
- **Passenger Type**: Linked to users or passengers.
- **Ticket Type**: Linked to the fare calculator to apply discounts or multipliers based on ticket type.
- **Time-Based Fare**: The fare multiplier applies based on the time of the journey.
- **Fare Capping**: Ensures passengers do not exceed daily/weekly maximums.
- **Fare Log**: Keeps a history of each trip fare calculation, including details on distance, zones, ticket type, passenger type, and applied fare rules.

### 4. **Sample Queries**

#### a. Calculate Distance-Based Fare
```sql
SELECT base_fare + (distance_traveled * rate_per_km) AS total_fare
FROM Distance_Fare
WHERE min_distance <= distance_traveled AND max_distance >= distance_traveled;
```

#### b. Calculate Zonal Fare
```sql
SELECT fare
FROM Zonal_Fare
WHERE zone_from = ? AND zone_to = ?;
```

#### c. Apply Passenger Discounts
```sql
SELECT fare * (1 - discount_rate) AS discounted_fare
FROM Passenger_Type
WHERE type = 'student';
```

#### d. Apply Peak/Off-Peak Multiplier
```sql
SELECT fare * fare_multiplier AS adjusted_fare
FROM Time_Fare
WHERE time_from <= NOW() AND time_to >= NOW() AND peak = TRUE;
```

#### e. Check Daily Fare Cap
```sql
SELECT cap_value
FROM Fare_Cap
WHERE cap_type = 'daily';
```

### 5. **Fare Calculation Process**

1. **Distance/Zonal Fare**: Depending on whether the system is distance-based or zonal, calculate the base fare.
2. **Passenger Type Discount**: Apply any discounts for passengers like students or seniors.
3. **Time-Based Adjustments**: Check whether the journey is in peak/off-peak time and apply a multiplier.
4. **Ticket Type**: Adjust fare based on ticket type (e.g., return or pass).
5. **Fare Capping**: If the user has a cap for the day/week, ensure they do not exceed the cap.
6. **Log the Calculation**: Store the calculated fare details in the `Fare Log` table.

### 6. **Additional Considerations**
- **Currency**: Include currency codes (e.g., USD, EUR) in relevant tables to accommodate multiple currencies.
- **Real-Time Updates**: If fares change (due to inflation, policy, etc.), ensure the database supports updating fares dynamically.

By designing a flexible schema like this, you'll be able to manage multiple fare structures, ticket types, and conditions in your metro rail simulator program, ensuring a realistic and dynamic fare system.