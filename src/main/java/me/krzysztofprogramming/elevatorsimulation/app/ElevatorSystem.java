package me.krzysztofprogramming.elevatorsimulation.app;

import lombok.Data;
import me.krzysztofprogramming.elevatorsimulation.app.model.Direction;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorRequest;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorStatus;
import me.krzysztofprogramming.elevatorsimulation.app.model.FloorData;

import java.util.*;
import java.util.stream.IntStream;

@Data
public class ElevatorSystem {
    private final int pickupCost;
    private final int releaseCost;
    private final int totalFloors;
    private final List<Elevator> elevatorsList;
    private final List<FloorData> floorsData;

    public ElevatorSystem(int pickupCost, int releaseCost, int totalFloors, int elevatorsCount) {
        this.pickupCost = pickupCost;
        this.releaseCost = releaseCost;
        this.totalFloors = totalFloors;
        this.elevatorsList = generateElevators(elevatorsCount);
        this.floorsData = generateFloorData(totalFloors);
    }

    private List<FloorData> generateFloorData(int totalFloors) {
        return IntStream.range(0, totalFloors).boxed()
                .map(floorNumber -> new FloorData(floorNumber, new LinkedList<>())).toList();
    }

    private List<Elevator> generateElevators(int amount) {
        return IntStream.range(1, amount + 1).boxed().map(this::createElevatorFromId).toList();
    }

    private Elevator createElevatorFromId(int id) {
        return new Elevator(id, 0, Collections.emptyList(), this);
    }

    public void runSimulationStep() {
        elevatorsList.forEach(elevator -> elevator.runElevatorSimulationStep(floorsData));
    }

    public void addNewRequest(ElevatorRequest elevatorRequest) {
        List<Integer> elevatorsEstimatedCosts = elevatorsList.stream()
                .map(elevator -> elevator.estimateWaitingCost(elevatorRequest)).toList();
        int maxIndex = IntStream.range(0, elevatorsEstimatedCosts.size()).boxed()
                .min(Comparator.comparing(elevatorsEstimatedCosts::get)).orElseThrow();

        Elevator optimalElevator = elevatorsList.get(maxIndex);
        optimalElevator.addNewRequest(elevatorRequest);
    }

    public List<Elevator> getFreeElevatorsOnFloor(int floorNumber) {
        return elevatorsList.stream().filter(elevator -> elevator.getCurrentFloor() == floorNumber && elevator.getSimulationData().isServeFloorNextTurn()).toList();
    }

    public void addNewCustomer(int startFloor, int targetFloor, boolean createRequest) {
        if (startFloor == targetFloor) return;
        floorsData.get(startFloor).targetFloors().add(targetFloor);
        List<Elevator> freeElevators = getFreeElevatorsOnFloor(startFloor);

        if (freeElevators.isEmpty()) {
            if (createRequest) addNewRequest(new ElevatorRequest(startFloor, Direction.of(startFloor, targetFloor)));
            return;
        }
        freeElevators.get(new Random().nextInt(0, freeElevators.size())).serveFloorIfCan(floorsData.get(startFloor));
    }

    public void addNewCustomer(int startFloor, int targetFloor) {
        this.addNewCustomer(startFloor, targetFloor, true);
    }

    public void addRequestToElevator(int elevatorId, ElevatorRequest elevatorRequest) {
        elevatorsList.get(elevatorId - 1).addNewRequest(elevatorRequest);
    }

    public List<ElevatorStatus> getElevatorStatuses() {
        return elevatorsList.stream().map(Elevator::getElevatorStatus).toList();
    }
}
