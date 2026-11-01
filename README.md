# Mars Colony Operations Simulator

A Java-based simulation of a Mars colony's operations, designed to model the challenges of maintaining a sustainable human settlement on Mars. The system manages personnel, robotic units, facilities, resources, missions, environmental conditions, and emergency response scenarios.

The project applies **Object-Oriented Programming (OOP)** principles, including inheritance, abstraction, encapsulation, and polymorphism, to represent the different entities and interactions within a Mars colony.

## Key Features

### 1. Personnel & Robotic Unit Management
- Manage human crew members with different specializations, including scientists, engineers, firefighters, and medical officers.
- Manage robotic units, including exploration rovers, repair robots, and drones.
- Track personnel health, fatigue, availability, and capabilities.
- Monitor robotic battery levels, operational status, and capabilities.
- Evaluate whether an agent is qualified and available to perform a required task.

### 2. Facility Management
- Model different colony facilities, including habitats, hospitals, laboratories, power stations, storage facilities, and communication stations.
- Track facility capacity, operational status, equipment, and available resources.
- Manage the assignment and movement of personnel and robotic units between facilities.
- Handle facility damage, destruction, and repair.

### 3. Resource Management
- Track essential colony resources, including:
  - Oxygen
  - Water
  - Food
  - Fuel
  - Battery power
  - Medical supplies
  - Spare parts
- Monitor resource availability across operational facilities.
- Allocate and deduct resources when missions and colony operations require them.
- Simulate resource consumption and replenishment over time.

### 4. Mission Management
The system supports different mission types:

- **Exploration:** Send exploration rovers to investigate different Martian terrains.
- **Research:** Conduct scientific research using qualified personnel.
- **Repair:** Restore damaged facilities and equipment.
- **Rescue:** Retrieve agents in crisis situations.

Missions include:
- Agent assignment and capability validation.
- Resource requirement checks.
- Mission state tracking.
- Duration and time progression.
- Failure handling based on resource availability, environmental conditions, and operational constraints.

### 5. Emergency Management
The system models emergency scenarios that require coordinated responses:

- **Fire:** Handle laboratory fires, damage facilities and equipment, and coordinate firefighting and medical assistance.
- **Medical Emergency:** Manage injured personnel and medical response.
- **Oxygen Leak:** Respond to oxygen-related incidents.
- **Rover Breakdown:** Handle exploration rover failures and coordinate repair operations.

Emergency responses involve identifying suitable responders, checking their capabilities and availability, and managing the resolution of incidents.

### 6. Environmental Conditions
- Model changing environmental conditions on Mars.
- Account for environmental effects on mission feasibility.
- Evaluate whether active missions can continue under changing conditions.

### 7. Colony Simulation
- Advance colony time and update active mission progress.
- Simulate food and water consumption.
- Replenish resources at scheduled intervals.
- Introduce environmental changes during simulation.

## Project Architecture

The project follows a modular, package-based structure. Related classes are grouped according to their responsibilities.

```text
src/
└── marscolony/
    ├── Equipment/
    │   ├── Equipment.java
    │   └── EquipmentType.java
    │
    ├── MarsColony/
    │   ├── MarsColony.java
    │   └── MarsSurface.java
    │
    ├── conditions/
    │   ├── Conditions.java
    │   └── EnvironmentCondition.java
    │
    ├── emergencies/
    │   ├── Emergency.java
    │   ├── EmergencyStatus.java
    │   ├── Fire.java
    │   ├── MedicalEmergency.java
    │   ├── OxygenLeak.java
    │   └── RoverBreakdown.java
    │
    ├── facilities/
    │   ├── Facilities.java
    │   ├── FacilityStatus.java
    │   ├── Habitat.java
    │   ├── Hospital.java
    │   ├── Laboratory.java
    │   ├── PowerStation.java
    │   ├── StorageFacility.java
    │   └── CommunicationStation.java
    │
    ├── missions/
    │   ├── Mission.java
    │   ├── MissionState.java
    │   ├── FailureReason.java
    │   ├── Capability.java
    │   ├── Exploration.java
    │   ├── Research.java
    │   ├── Repair.java
    │   └── Rescue.java
    │
    ├── personnel/
    │   ├── Agent.java
    │   ├── Human.java
    │   ├── RoboticUnit.java
    │   ├── Scientist.java
    │   ├── Engineer.java
    │   ├── Firefighter.java
    │   ├── MedicalOfficer.java
    │   ├── ExplorationRover.java
    │   ├── RepairRobot.java
    │   ├── Drone.java
    │   ├── HealthStatus.java
    │   ├── FatigueLevel.java
    │   └── BatteryLevel.java
    │
    ├── resources/
    │   ├── Resource.java
    │   └── ResourceType.java
    │
    └── Main.java
```

## Object-Oriented Design

The system uses inheritance and abstraction to organize related entities and avoid unnecessary code duplication.

Some of its main class hierarchies include:

**Personnel hierarchy**
- `Agent` is the abstract base class.
- `Human` and `RoboticUnit` extend `Agent`.
- Specialized human and robotic classes extend their respective parent classes.

**Mission hierarchy**
- `Mission` defines the common mission structure and behavior.
- `Exploration`, `Research`, `Repair`, and `Rescue` implement specialized mission requirements and behavior.

**Emergency hierarchy**
- `Emergency` defines the common emergency structure and response interface.
- `Fire`, `MedicalEmergency`, `OxygenLeak`, and `RoverBreakdown` implement specialized emergency scenarios.

**Facility hierarchy**
- `Facilities` provides the common facility structure.
- Specific facility classes extend it or, in the case of `Hospital`, extend `Habitat`.

Polymorphism allows the system to handle different agents, missions, facilities, and emergencies through their common parent types.

## Technologies Used

- **Programming Language:** Java
- **Development Environment:** Visual Studio Code
- **Version Control:** Git & GitHub
- **Java Development Tools:** JDK and Java Compiler

## The UML diagram links

- **Usecase Diagram:** https://tinyurl.com/53a5tcej
- **Sequence Diagram 1:** https://tinyurl.com/4t3zdsxt
- **Sequence Diagram 2:** https://tinyurl.com/57ay3m92
- **Sequence Diagram 3:** https://tinyurl.com/2x9e58jh
- **Object Diagram:** https://tinyurl.com/cp5khdnc
- **Class Diagram:** https://tinyurl.com/v83awr79
- **State Machine Diagram:** https://tinyurl.com/48488b7y

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed.
- Visual Studio Code (or another Java-compatible IDE).
- Git (optional, for cloning the repository).

### Clone the Repository

```bash
git clone https://github.com/aya-saleh123/mars-ops-colony-simulator.git
```

Navigate to the project directory:

```bash
cd mars-ops-colony-simulator
```

### Open the Project

Open the project folder in Visual Studio Code.

The Java source directory is configured as `src`, and compiled `.class` files are generated in `bin`.

### Compile the Project

From the project root, run the following command in PowerShell:

```powershell
javac -d bin (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
```

### Run the Integration Tests

The project currently includes manual integration tests for the fire emergency response workflow.

After compiling, execute:

```bash
java -cp bin marscolony.FireEmergencyTests
```

The test suite checks scenarios involving:
- Successful fire emergency resolution.
- Fire emergencies without patients.
- Insufficient oxygen.
- Missing firefighters or medical officers.
- Missing or unavailable hospitals.
- Insufficient medical supplies.
- Patient health and facility transfers.
- Mission rejection when personnel are unhealthy.

The test runner reports the number of passed and failed tests.

## Contributors

- Aya Saleh
- Yara Sameh
- Mariam El-Habashy
- Toka Hassan

Developed collaboratively as a Java Object-Oriented Programming project.

