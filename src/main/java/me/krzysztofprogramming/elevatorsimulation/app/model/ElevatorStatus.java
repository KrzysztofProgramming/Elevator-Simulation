package me.krzysztofprogramming.elevatorsimulation.app.model;

import java.util.List;

public record ElevatorStatus(int elevatorId, int currentFloor, List<Integer> elevatorTargets) {
}
