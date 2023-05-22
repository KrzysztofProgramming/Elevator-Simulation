package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.krzysztofprogramming.elevatorsimulation.app.ElevatorSystem;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
public class AddNewElevatorRequestRequest {
    private int startFloor;
    private int direction;

    public void validate(ElevatorSystem elevatorSystem) {
        List<String> errors = new LinkedList<>();
        if (startFloor >= elevatorSystem.getTotalFloors() || startFloor < 0) errors.add("startFloor wrong range");
        if (direction == 0) errors.add("Direction cannot be 0");
        if (direction < 0 && startFloor == 0) errors.add("Cannot go down from 0");
        else if (direction > 0 && startFloor == elevatorSystem.getTotalFloors() - 1)
            errors.add("Cannot go up from " + startFloor);
        if (!errors.isEmpty()) throw new ValidationFailedException(errors);
    }
}
