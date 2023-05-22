package me.krzysztofprogramming.elevatorsimulation.app;

import lombok.Data;
import me.krzysztofprogramming.elevatorsimulation.app.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import static me.krzysztofprogramming.elevatorsimulation.app.model.TargetType.RELEASE;

@Data
public class Elevator {
    private final int elevatorId;
    private final List<ElevatorTarget> targetFloors;
    private final ElevatorSystem elevatorSystem;
    private final ElevatorSimulationData simulationData = new ElevatorSimulationData();
    private int currentFloor;

    public Elevator(int elevatorId, int currentFloor, List<Integer> targetFloors, ElevatorSystem elevatorSystem) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.targetFloors = mapToTargetFloors(targetFloors);
        this.elevatorSystem = elevatorSystem;
    }

    public Elevator(int elevatorId, int currentFloor, List<Integer> targetFloors, ElevatorSystem elevatorSystem, Direction lastTargetDirection) {
        this(elevatorId, currentFloor, targetFloors, elevatorSystem);
        if (this.targetFloors.isEmpty()) return;
        this.targetFloors.get(this.targetFloors.size() - 1).setNextDirection(lastTargetDirection);
    }

    public Elevator(int elevatorId, int currentFloor, ElevatorSystem elevatorSystem, List<ElevatorTarget> targetFloors) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.targetFloors = new LinkedList<>(targetFloors);
        this.elevatorSystem = elevatorSystem;
    }

    public ElevatorStatus getElevatorStatus() {
        return new ElevatorStatus(this.elevatorId, this.currentFloor,
                this.targetFloors.stream().map(ElevatorTarget::getTargetFloor).toList());
    }

    public void runElevatorSimulationStep(List<FloorData> floorDataList) {
        if (this.simulationData.isServeFloorNextTurn() || this.targetFloors.isEmpty()) {
            this.simulationData.setServeFloorNextTurn(this.targetFloors.isEmpty());
            serveFloor(floorDataList.get(this.currentFloor));
            return;
        }
        Direction currentDirection = getCurrentDirection();
        this.currentFloor += currentDirection.getValue();
        if (this.currentFloor == this.targetFloors.get(0).getTargetFloor()) {
            this.targetFloors.removeIf(target -> target.getTargetFloor() == this.currentFloor);
            this.simulationData.setServeFloorNextTurn(true);
        }
    }

    private Direction getCurrentDirection() {
        return Direction.of(this.currentFloor, this.targetFloors.get(0).getTargetFloor());
    }

    public void serveFloorIfCan(FloorData floorData) {
        if (!this.simulationData.isServeFloorNextTurn() || floorData.floorNumber() != this.currentFloor) return;

        serveFloor(floorData);
    }

    private void serveFloor(FloorData floorData) {
        if (floorData.targetFloors().isEmpty()) return;
        floorData.targetFloors().forEach(this::addNewReleaseTarget);
        floorData.targetFloors().clear();
    }

    private List<ElevatorTarget> mapToTargetFloors(List<Integer> list) {
        LinkedList<ElevatorTarget> mappedRequest = new LinkedList<>();
        if (list.isEmpty()) return mappedRequest;
        mappedRequest.add(new ElevatorTarget(list.get(0), null, RELEASE));
        for (int i = 1; i < list.size(); i++) {
            mappedRequest.getLast().setNextDirection(Direction.of(list.get(i) - list.get(i - 1)));
            mappedRequest.add(new ElevatorTarget(list.get(i), null, RELEASE));
        }
        return mappedRequest;
    }

    public void addNewRequest(ElevatorRequest request) {
        addNewTarget(request.getStartFloor(), request.getNextDirection(), TargetType.PICKUP, Function.identity());
    }

    public void addNewReleaseTarget(int targetFloor) {
        addNewTarget(targetFloor, null, RELEASE, index -> Math.min(getIndexAfterLastReleaseTarget(), index));
    }

    private void addNewTarget(int targetFloor, Direction direction, TargetType targetType, Function<Integer, Integer> indexMapper) {
        if (targetFloor == this.currentFloor) return;
        if (targetFloors.isEmpty()) {
            targetFloors.add(new ElevatorTarget(targetFloor, direction, targetType));
            return;
        }
        if (targetFloors.stream().anyMatch(target -> target.getTargetFloor() == targetFloor)) return;


        ListIterator<ElevatorTarget> targetFloorsIterator = targetFloors.listIterator();
        ElevatorTarget currentTarget = new ElevatorTarget(this.currentFloor, getCurrentDirection(), null);
        int i;
        for (i = 0; targetFloorsIterator.hasNext(); i++) {
            ElevatorTarget nextTarget = targetFloorsIterator.next();
            Direction currentDirection = Direction.of(targetFloor, nextTarget.getTargetFloor());
            boolean canAddPickupDown = targetType == RELEASE || currentDirection == Direction.DOWN && direction == Direction.DOWN;
            boolean canAddPickupUp = targetType == RELEASE || currentDirection == Direction.UP && direction == Direction.UP;

            boolean canAddEarlier = currentTarget.getTargetFloor() < targetFloor && targetFloor < nextTarget.getTargetFloor() && canAddPickupUp ||
                    currentTarget.getTargetFloor() > targetFloor && targetFloor > nextTarget.getTargetFloor() && canAddPickupDown;

            currentTarget = nextTarget;
            if (canAddEarlier) {
                insertNewTargetFloor(i, targetFloor, targetType, direction);
                return;
            }
        }
        int index = indexMapper.apply(i);
        insertNewTargetFloor(index, targetFloor, targetType, direction);
    }

    public int getIndexAfterLastReleaseTarget() {
        ListIterator<ElevatorTarget> iterator = targetFloors.listIterator(targetFloors.size());
        int index = targetFloors.size();
        while (iterator.hasPrevious()) {
            if (iterator.previous().getTargetType() == RELEASE) return index;
            index--;
        }
        return index;
    }

    private void insertNewTargetFloor(int index, int targetFloor, TargetType targetType, Direction defaultDirection) {
        ListIterator<ElevatorTarget> iterator = targetFloors.listIterator(index);
        if (iterator.hasPrevious()) {
            ElevatorTarget previousTarget = iterator.previous();
            previousTarget.setNextDirection(Direction.of(previousTarget.getTargetFloor(), targetFloor));
            iterator.next();
        }
        if (iterator.hasNext()) {
            ElevatorTarget nextTarget = iterator.next();
            iterator.previous();
            iterator.add(new ElevatorTarget(targetFloor, Direction.of(targetFloor, nextTarget.getTargetFloor()), targetType));
            return;
        }
        iterator.add(new ElevatorTarget(targetFloor, defaultDirection, targetType));
    }

    public int estimateWaitingCost(ElevatorRequest newRequest) {
        if (targetFloors.isEmpty()) return calcFloorTime(this.currentFloor, newRequest.getStartFloor(), RELEASE);

        ElevatorTarget currentTarget = new ElevatorTarget(this.currentFloor,
                Direction.of(this.currentFloor, targetFloors.get(0).getTargetFloor()), RELEASE);
        int totalWaitingTime = 0;
        ElevatorTarget nextTarget;

        for (ElevatorTarget targetFloor : targetFloors) {
            nextTarget = targetFloor;

            boolean canPickupEarlierDown = calcPickupEarlierDown(newRequest, currentTarget)
                    && nextTarget.getTargetFloor() < newRequest.getStartFloor();
            boolean canPickupEarlierUp = calcPickupEarlierUp(newRequest, currentTarget)
                    && nextTarget.getTargetFloor() > newRequest.getStartFloor();

            if (canPickupEarlierDown || canPickupEarlierUp) {
                return totalWaitingTime + calcFloorTime(currentTarget.getTargetFloor(), newRequest.getStartFloor(), currentTarget.getTargetType());
            } else {
                totalWaitingTime += calcFloorTime(currentTarget.getTargetFloor(), nextTarget.getTargetFloor(), currentTarget.getTargetType());
                currentTarget = nextTarget;
                if (currentTarget.getTargetFloor() == newRequest.getStartFloor()) return totalWaitingTime;
            }
        }
        if (currentTarget.getNextDirection() == null ||
                calcPickupEarlierDown(newRequest, currentTarget) ||
                calcPickupEarlierUp(newRequest, currentTarget)) {
            return totalWaitingTime + calcFloorTime(currentTarget.getTargetFloor(), newRequest.getStartFloor(), currentTarget.getTargetType());
        }

        return totalWaitingTime + (currentTarget.getNextDirection() == Direction.DOWN ?
                calcFloorTime(currentTarget.getTargetFloor(), 0, currentTarget.getTargetType())
                        + calcFloorTime(0, newRequest.getStartFloor(), currentTarget.getTargetType()) :
                calcFloorTime(currentTarget.getTargetFloor(), elevatorSystem.getTotalFloors() - 1, currentTarget.getTargetType())
                        + calcFloorTime(currentTarget.getTargetFloor(), elevatorSystem.getTotalFloors() - 1, currentTarget.getTargetType()));
    }

    private boolean calcPickupEarlierDown(ElevatorRequest newRequest, ElevatorTarget currentRequest) {
        return newRequest.getNextDirection() == Direction.DOWN && currentRequest.getNextDirection() == Direction.DOWN
                && currentRequest.getTargetFloor() > newRequest.getStartFloor();
    }

    private boolean calcPickupEarlierUp(ElevatorRequest newRequest, ElevatorTarget currentRequest) {
        return newRequest.getNextDirection() == Direction.UP && currentRequest.getNextDirection() == Direction.UP
                && currentRequest.getTargetFloor() < newRequest.getStartFloor();
    }

    private int calcFloorTime(int floorA, int floorB, TargetType targetType) {
        return Math.abs(floorA - floorB) +
                (targetType == RELEASE ? this.elevatorSystem.getReleaseCost() : this.elevatorSystem.getPickupCost());
    }

}
