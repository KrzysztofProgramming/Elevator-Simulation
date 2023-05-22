package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ElevatorData {
    private int elevatorId;
    private int currentFloor;
    private int numberOfPassengers;
    private List<Integer> targetFloors;
}
