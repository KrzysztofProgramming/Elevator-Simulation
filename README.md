## About

The project has two parts, fronted written in Typescript with Angular and backend written in Java with Spring Boot.
The simulation and all the logic take place on the backend side.

### How it works?

The system estimates which one of the available elevators will transport the client to the desired location the soonest
then it adds a pickup request to the elevator's queue. The order of serving floors differs from FCFS
(first-come, first-serve) in one way. The elevator may serve later request sooner if it's on its current path.
For example, it will stop picking up the user wanting to go from floor 5 to 0 if it's currently moving from floor 8 to 1
The elevator also tries to sort selected floors. If someone selects (9, 2, 6) on floor 0, the elevator will change it
to (2, 6, 9).

### Running the program

There are two ways to run the program

1. Open the project in your IDE, compile and click the "run" button.
2. There is a compiled .jar version of the program elevatorsimulation-compiled.jar in the repository. You can use the
   command
   `java -jar elevatorsimulation-compiled.jar` to start the program.   
   After you run the program go to http://localhost:8080.

### Compiling the program

Execute `mvn package`, this will create a directory "target" with the compiled .jar file.
If you want to compile frontend sources as well before you compile Java files navigate to ElevatorSimulationFrontend
directory and
run `ng build` (you need Angular 16.0.2 installed to do this).

### How to use?
![image](https://github.com/KrzysztofProgramming/Elevator-Simulation/assets/61599048/d7c771e2-2f16-48c9-820c-81a9a03b828a)
