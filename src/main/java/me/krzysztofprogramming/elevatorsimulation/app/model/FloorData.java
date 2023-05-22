package me.krzysztofprogramming.elevatorsimulation.app.model;

import java.util.List;

public record FloorData(int floorNumber, List<Integer> targetFloors) {
}
