### Project 4: Disease simulation by Fermin Ramos and Loc Tung Su

### Assumption: 
1. In the configuration file, the "dimensions" always followed by "random"

### Agent Status
Agents can have 5 types of status:
1. Vulnerable
2. Sick
3. Dead
4. Recovered
5. Immune

Recovered and Immune are treated the same way. They cannot be sick. However,
there are important distinctions between Recovered and Immune. Immune Agents
are initialized as immune, they will never get sick. Recovered Agents were
sick, but recovered enough to gain immunity from the disease.

### Features:
1. Added a status Immune for the agent that can "fight" against the "virus"
2. In addition, added a feature around the Immune status where there is possibility of the immune system
   can no fight back the "virus"
3. Added a history board that display the status change throughout the spread.
   For instance, on day 1, agent 1 is sick, agent 2 is dead, etc. It will stop recording when the all the "die-able" agents are dead.
4. Added a live line graph that show the number of dead throughout each day.

### GUI "Death Totals" Graph
Graph illustrates the total dead over the course of the program. If an agent
dies, it adds +1 to the total dead counter. Likewise, the graph line only
goes up as more total agents die from the disease.

### How to run
There are two ways to run the program:
1. Run it through Main.java:
    + Click the green triangle on the top right.
    + When it runs, in the terminal, it will ask the user to input the test file of choice in the resource folder. 
i.e: "test1.txt", "test2.txt"
    + Then it will ask to input the number of second that you want to represent a "day pass".
If you want a fast simulation, enter a lower number like 1 or 2 as the animation will update every 1 or 2 second
as a "day pass". If you want a slow simulation, enter a high number like 7 or 8 as the animation will update every 7 or 8 second
as a "day pass".
    + The GUI will pop up after you enter the configuration test file of choice and the daySecDuration.
    + Hit the run button to start the simulation.
2. Run it through the jar file:
    + You can run the jar file by typing this command "java -jar jar-file-name.jar"
    + You can also just double-click the jar file itself.

### Team's participation
Loc Su mainly focus on reading in the configuration file, distribute the information and the GUI.
Fermin Ramos mainly focus on the thread work, the back end. But both contribute a lot in helping each other part when one got stuck.

In Main.java:
+ Loc Su is responsible for the neighbour storing implementation, the distance between agents, the GUI that fit 
the configuration file description, the immunity and sickness implementation.
+ Fermin is responsible for making the live line chart works based on the thread interaction, the history pane that record the simulation changes
, and the run button that allow the program to start.

In Grid.java and Dimension.java:
+ Loc Su is responsible for those

In ConfigReader.java:
+ Loc Su is the main author, with some slight change from Fermin.

In the Agent.java:
+ Fermin is the main author of it, he wrote all the thread interaction, blockingqueue,etc. Loc Su is only responsible for making the constructor and make it a part of the Circle family

In the Time.java:
+ Fermin is responsible for implementing the time and Loc Su uses that information to create the Animation change through the AnimationTimer

In the Status.java:
+ Fermin is the author.


