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
<Who will use this system?>

BRIEF DESCRIPTION:


CORE OOP CONCEPTS:
- Encapsulation: Classes like Reservation, Trip, and Passenger keep their data private and expose only necessary methods (e.g., reserveSeat(), cancelReservation()). <br>
- Inheritance: Passenger, Driver, and Administrator inherit from a common User class.<br>
- Polymorphism: Different user types override methods such as displayDashboard() or performAction().<br>
- Abstraction: Interfaces or abstract classes such as Validator, Repository, or User hide implementation details from the GUI.<br>

INITIAL CLASS IDEAS:
- ClassName1: <responsibility>
- ClassName2: <responsibility>
- ClassName3: <responsibility>

USER STORIES (Recommended):
- As a DLSU student, I want to see the current seat reservation progress so I know if a certain bus schedule is full or not.
- As a regular shuttle user going to Laguna and back, I want to know the actual schedules of each bus so that I can know the time a bus would come or go.
- As a new user of the shuttle service reservation, I want the platform to be well organized so that I am given the correct and overall information I need to get on the bus.

CORE FEATURES (Recommended):
- <Feature 1>
- <Feature 2>
- <Feature 3>
