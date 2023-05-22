package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.krzysztofprogramming.elevatorsimulation.app.model.FloorData;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SimulationData {
    private int totalFloors;
    private int numberOfElevators;
    private List<ElevatorData> elevatorsList;
    private List<FloorData> floorDataList;
}
