package me.krzysztofprogramming.elevatorsimulation.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElevatorRequest {
    private int startFloor;
    private Direction nextDirection;
}
