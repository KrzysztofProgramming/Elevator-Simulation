package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateNewSimulationRequest {
    private int totalFloors;
    private int numberOfElevators;

    public void validate() {
        List<String> errors = new LinkedList<>();
        if (totalFloors <= 0) errors.add("totalFloors is 0");
        if (numberOfElevators <= 0) errors.add("numberOfElevators is 0");
        if (!errors.isEmpty()) throw new ValidationFailedException(errors);
    }
}
