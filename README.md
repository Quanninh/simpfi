# SiMpFi - Real-Time Traffic Simulation with Java

**Instructor:** Prof. Dr.-Eng. Ghadi Mahmoudi

**Team Members:**
- Kenji Bischoff
- Nguyễn Hà Khải
- Nguyễn Duy Khánh
- Tô Huỳnh Phúc
- Ninh Hoàng Quân

---

# Introduction
- SUMO - Simulation of Urban MObility - is a traffic simulation tool used to simulate urban transportation scenarios. The application is helpful for designers, researchers who design road network and evaluate features such as CO2 levels, traffic congestion rate, etc.
- Although providing useful functionalities for traffic simulation, SUMO is difficult to utilize effectively. In other words, only users with programming knowledge can benefit greatly from the software. This disadvantage can be observed by the fact that users are required to grasp a basic understanding of SUMO’s configurations such as XML files and how to run command lines.
- In order to address the problem mentioned above, the team aims to create a software that leverages SUMO’s features but maintains the simplicity of user interface. That is, people can perform simple operations such as drag-and-drop and clicking buttons to retrieve meaningful detailed information and represent complicated traffic networks. Consequently, more and more designs can be sketched and created quickly, which potentially speeds up one’s studies or transportation planning. Furthermore, an increased number of designs means that the urban area may have better transportation plans, thereby reducing traffic congestion and CO2 emissions.
- Regarding study goals, the team plans to master in Object-Oriented Programming concepts and gain more experience with teamwork, writing documents such as javadoc and academic reports.

# Project Overview
## Introduction
The software **simpfi**, which stands for **Si**mulation **M**a**p** Traf**fi**c, is a traffic simulation tool used to create and monitor simulated representation of real-world networks. For clarity, the application’s core is based on SUMO, but the difference is that it is developed to suit a wider range of target users from non-coders to experts.

## Technology Stack
There are three main components the team uses for the project: Java, TraaS, and SUMO.

Additionally, the team uses GitHub to create a shared repository for members to submit codes, and Notion to distribute tasks.

## Architecture diagram
From the diagram, three main layers can be observed: GUI & Visualization, SUMO Integration, and Data. Each layer provides separate methods in order to guarantee modularity and coherence in the system’s design.

### GUI & Visualization Layer
The main task of this layer is to present an user-friendly UI for users. In particular, it includes
map visualization, multiple panels such as Inject so that users can add vehicles to the net-
work. Additionally, users can move and scale the map by using keyboard shortcuts like Ctrl +
(command +) to increase its size.

### SUMO Integration (Wrapper) Layer
The layer is where TraCI connection takes place. Put differently, it is the interface where
SUMO and wrapper classes communicate with each other. From there, the simulation can be
executed, logical operations like numerical calculations can be performed, and network controls
can be handled flawlessly.

### Data Layer
As its name suggests, this layer handle data-related operations such as live storage and past
storage, which is essential for exporting meaningful statistics. Also, the layer is developed to
ensure the consistency and integrity of data throughout different parts of the system.

## Class Design

### Overview
Besides core wrapper classes, the team decided to draw the user interface by defining and
connecting classes such as buttons, panels,... As a result, the number of classes was up to 30,
which would be excessively complicated and difficult to understand if the team sketch them
in one single class diagram. Therefore, a wrapper class diagram was designed to ensure the
conciseness and readability.

### Class Responsibilities
- VehicleController: its main role is to process and retrieve detailed information regarding
vehicles. To achieve this, the class has a SumoTraciConnection attribute conn used to actively
connect to SUMO so that getters such as getSpeed(), getRoadID() can be implemented. Additionally, vehicleCounter and vehicleMap are developed to keep tracks of vehicles number
and mapping. Furthermore, the class provides several useful methods like setVehicles(),
addVehicle() for users to perform vehicle-related operations.
- TrafficLightController: as its name suggests, the class deals with traffic light objects, especially in the real-time scenarios since it has a live traffic light mapping: liveTrafficLightStates.
Similar to VehicleController, the class establishes a connection with SUMO using the attribute conn and therefore is able to implement getters such as getIDList(), getState().
Besides, updateTrafficLightState(), getLiveTrafficLightStates() are provided to maintain the logic of SUMO traffic lights in the Simpfi’s map.
- SumoConnectionManager: is the class that creates and manages the TraCI connection between SUMO and the project’s app. This is achieved by passing a XML configuration file to its
constructor and then the program can initialize the connection by getConnection() method and close it safely later using close(). Moreover, it provides an important method doStep()
which advances the simulation by one timestep.
- App: is where the main function resides. Particularly, it connects the frontend (mapPanel)
and the backend (vehicleController and trafficLightController); defines the TraCI connection; and handles the connection loop so that the software can function properly.



## GUI Mockups
<img width="1437" height="844" alt="Screenshot 2025-11-27 at 23 17 37" src="https://github.com/user-attachments/assets/ce35e8ea-1ea5-40b1-8718-5456802998b3" />



# Run Instructions
- Download project folder
- Open project folder in Eclipse
- Go to `src/main/java/com/simpfi/lib` folder
- Right click the TraaS.jar file and the flatlaf-3.6.2.jar file and go to Build Path > Add to Build Path
- Compile and run App.java
- In order to change the map, go to `scr/main/java/com/simpfi/config/Constants.java` and replace the 3 path at the top of the file with your own configurations. We have also included another configuration that you can uncomment and comment the original files to use it.
