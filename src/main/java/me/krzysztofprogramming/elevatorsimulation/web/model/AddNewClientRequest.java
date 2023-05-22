package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.krzysztofprogramming.elevatorsimulation.app.ElevatorSystem;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class AddNewClientRequest {
    private int startFloor;
    private int endFloor;
    private boolean createRequest = true;

    public void validate(ElevatorSystem elevatorSystem) {
        List<String> errors = new LinkedList<>();
        if (startFloor >= elevatorSystem.getTotalFloors() || startFloor < 0) errors.add("startFloor has wrong range");
        if (endFloor >= elevatorSystem.getTotalFloors() || endFloor < 0) errors.add("end has wrong range");
        if (!errors.isEmpty()) throw new ValidationFailedException(errors);
    }
}
