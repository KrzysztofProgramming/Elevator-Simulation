package me.krzysztofprogramming.elevatorsimulation.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElevatorTarget {
    private int targetFloor;
    private Direction nextDirection;
    private TargetType targetType;
}
