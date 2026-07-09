PROJECT TITLE:
AniLink: DLSU Shuttle Service Reservation

TEAM MEMBERS:<br>
Caeljan D. Cristobal - ccaeljan-lang<br>
Froilene pangit - GitHub username<br>
Shawn Derek A. Paingan - GitHub username<br>

PROBLEM STATEMENT & GOALS:<br>
Goals:
To develop a centralized shuttle management platform that improves the efficiency, accessibility, and reliability of the DLSU shuttle service.
1. To provide a digital platform for shuttle scheduling and seat reservations.
2. To enable real-time shuttle tracking and QR code-based/ticket/card passenger verification.
3. To simplify shuttle operations by improving coordination among passengers, drivers, and administrators.

Problem Statement:
1. Repeated Manual Data Entry: Passengers are required to repeatedly provide the same personal and trip information for every reservation, making the process time-consuming and increasing the likelihood of incomplete or incorrect submissions.
2. Inefficient Reservation Management: Because reservations are handled through standalone online forms, the system lacks proper conflict detection and schedule management, making it difficult for users to organize multiple trips and for administrators to monitor seat availability accurately.
3. Time-Consuming Passenger Verification: Boarding relies on manually checking passenger names against reservation lists, which slows down the boarding process, creates queues at pickup locations, and increases the risk of verification errors.


TARGET USER:

AniLink is intended to help serve the community of De La Salle University (DLSU). The platform has three specific categories for its target users.

1. Passengers
DLSU Students (Undergraduate and Graduate): Students with Inter-campus enrolled subjects or in thesis using Inter-campus facilities. They will use the platform to reserve seats, view departure schedules, monitor their status, and generate QR codes/tickets/cards for a seamless boarding.

DLSU Faculty and Staffs: Faculty with Inter-campus assignments.

2. Shuttle Drivers and Administrators
Assigned DLSU Drivers and Facilitators: They are responsible for monitoring and operating the DLSU Shuttle. They will use the platform to ensure a smooth boarding process by scanning the QR codes or validating the passengers' tickets or cards. They will also use the platform to monitor and update real-time data such as the time and status of their trip. 


BRIEF DESCRIPTION:


CORE OOP CONCEPTS:
- Encapsulation: Classes like Reservation, Trip, and Passenger keep their data private and expose only necessary methods (e.g., reserveSeat(), cancelReservation()). <br>
- Inheritance: Passenger, Driver, and Administrator inherit from a common User class.<br>
- Polymorphism: Different user types override methods such as displayDashboard() or performAction().<br>
- Abstraction: Interfaces or abstract classes such as Validator, Repository, or User hide implementation details from the GUI.<br>

INITIAL CLASS IDEAS:

1. User (Abstract Class) - Parent class and responsible for encapsulating shared attributes like userName/userID, name, and email.
2. Passenger (Inherits User) - Represents the DLSU Students and Faculties and responsible for tracking their personal reservations history.
3. Driver (Inherits User) - For the Assigned Shuttle Drives and responsible for passenger verification and updating real-time data. 
4. Administrator (Inherits User) - For the campus transport administrator and responsible for system configuration.
5. Route - Represent the travel path and responsible for aggregating its associated departure schedules and validating if a passenger has already booked this specific path today.
6. DepartureSchedule - Represents the planned timeslot.
7. Trip - The physical shuttle bus.
8. Reservation - Booking transaction and responsible for linking Passenger to a Trip.
9. ReservationManager (Controller) - Acts as the central logic handler. Responsible for processing the first-in, first-out (FIFO) waitlist promotion when someone cancels, and enforcing the business rules (Category Limit and Route Limit validations) before finalizing a Reservation object.


USER STORIES (Recommended):
- As a DLSU student, I want to see the current seat reservation progress so I know if a certain bus schedule is full or not.
- As a regular shuttle user going to Laguna and back, I want to know the actual schedules of each bus so that I can know the time a bus would come or go.
- As a new user of the shuttle service reservation, I want the platform to be well organized so that I am given the correct and overall information I need to get on the bus.

CORE FEATURES (Recommended):
- <Feature 1>
- <Feature 2>
- <Feature 3>
