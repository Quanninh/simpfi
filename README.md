# SiMpFi - Real-Time Traffic Simulation with Java

**Instructor:** Prof. Dr.-Eng. Ghadi Mahmoudi
**Team Members:**
- Kenji Bischoff
- Nguyễn Hà Khải
- Nguyễn Duy Khánh
- Tô Huỳnh Phúc
- Ninh Hoàng Quân

---

# Project Overview
- This project is to create a real-time traffic simulation platform via SUMO mobility engine, controlled via the TraaS Java API.
- The team aims to provide an user-friendly, interactive and useful software tool with functions that leverage SUMO's existing
features to help users simulate traffic scenarios and extract meaningful statistics.

## Milestone 1
### Architecture diagram
- Visualize the structure(put the upperlink here) to describe how the application works and explain how the set of components in project interact.
### Class Design for TraaS wrapper (Vehicle, TrafficLight, etc.)
- Identify requirements and design a detailed class diagram(put the hyperlink here).
- Write a Java program that encapsulate TraaS API calls and object-oriented methods. 
- The program provides control over the SUMO simulation to retrieve the real-time data collected from vehicles, traffic lights and other simulation entities.
- Additionally, new functionalities and classes are developed to support more realistic real-world scenarios.  
### GUI Mockups
- Create a new interface that illustrate the arrangement of buttons, maps, menu bar,...
- Important user interface mockups are listed below:
(click here to see the full size)
- Full GUI mockups can be found here(put the upper link here: may be to figma or sth)
### SUMO Connection Demo
- Important Notice: You should run our example using Java (version), SUMO (version), and TraaS (version) to ensure the expected result

1. Ensure successful download of Java, SUMO, and TraaS. (put the upper link in 3 words link to offcial download pages)

2. Open terminal and select your preferred directory, then create ...
(show the code)

**Note**:  

*For Windows Users*: you can open this project in Eclipse and run directly in that IDE  

*For Linux and Mac Users*: Once you open the project in Eclipse, you must go to  
`YOUR_PATH/simpfi/src/main/java/com/simpfi/sumo/wrapper/SumoConnectionManager.java`  
and change "sumo-gui" in  
`ProcessBuilder pb = new ProcessBuilder("sumo-gui", "-c", cfg,"--start", "--quit-on-end", "--remote-port", "9999", "--step-length", "0.1");`  
to your sumo-gui path (type `which sumo-gui` in the terminal to find the path)

3. Combine the code by creating .sumoconfg file: ...

4. Now create a java file and type in: ...

5. Make sure you saved all the afformentioned files, then run: ...
If it functions correctly, a panel with a traffic scenario should pop up on your device. Congrats 🎉🎉🎉 

### Techonology Stack
- The team uses Java, SUMO, and TraaS as the main technology to develop our software.

- Addtionally, Notion is leveraged to track progress, monitor tasks and contributions of team member. You can find our detailed activity here.(put the Notion link)
