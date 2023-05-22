package me.krzysztofprogramming.elevatorsimulation.web;

import me.krzysztofprogramming.elevatorsimulation.app.Elevator;
import me.krzysztofprogramming.elevatorsimulation.app.ElevatorSystem;
import me.krzysztofprogramming.elevatorsimulation.app.model.Direction;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorRequest;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorTarget;
import me.krzysztofprogramming.elevatorsimulation.app.model.TargetType;
import me.krzysztofprogramming.elevatorsimulation.web.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimulationController {

    private ElevatorSystem systemSimulation;

    public SimulationController() {
        this.systemSimulation = new ElevatorSystem(6, 2, 10, 3);
    }

    private SimulationData mapToSimulationData(ElevatorSystem elevatorSystem) {
        return new SimulationData(elevatorSystem.getTotalFloors(), elevatorSystem.getElevatorsList().size(),
                elevatorSystem.getElevatorsList().stream().map(this::mapToElevatorData).toList(), systemSimulation.getFloorsData());
    }

    private ElevatorData mapToElevatorData(Elevator elevator) {
        return new ElevatorData(
                elevator.getElevatorId(), elevator.getCurrentFloor(),
                Math.toIntExact(elevator.getTargetFloors().stream()
                        .filter(elevatorTarget -> elevatorTarget.getTargetType() == TargetType.RELEASE).count()),
                elevator.getTargetFloors().stream().map(ElevatorTarget::getTargetFloor).toList()
        );
    }

    @GetMapping("/getSimulationData")
    @ResponseStatus(HttpStatus.OK)
    public SimulationData getSimulationData() {
        return mapToSimulationData(this.systemSimulation);
    }

    @PostMapping("/addNewClient")
    @ResponseStatus(HttpStatus.OK)
    public SimulationData addNewClient(@RequestBody AddNewClientRequest addNewClientRequest) {
        addNewClientRequest.validate(this.systemSimulation);
        this.systemSimulation.addNewCustomer(addNewClientRequest.getStartFloor(), addNewClientRequest.getEndFloor(), addNewClientRequest.isCreateRequest());
        return getSimulationData();
    }

    @PostMapping("/addNewElevatorRequest")
    @ResponseStatus(HttpStatus.OK)
    public SimulationData addNewElevatorRequest(@RequestBody AddNewElevatorRequestRequest request) {
        request.validate(this.systemSimulation);
        this.systemSimulation.addNewRequest(new ElevatorRequest(request.getStartFloor(), Direction.of(request.getDirection())));
        return getSimulationData();
    }

    @PostMapping("/createNewSimulation")
    @ResponseStatus(HttpStatus.OK)
    public SimulationData createNewSimulation(@RequestBody CreateNewSimulationRequest request) {
        request.validate();
        this.systemSimulation = new ElevatorSystem(6, 2, request.getTotalFloors(), request.getNumberOfElevators());
        return getSimulationData();
    }

    @PostMapping("/runSimulationStep")
    @ResponseStatus(HttpStatus.OK)
    public SimulationData createNewSimulation() {
        this.systemSimulation.runSimulationStep();
        return getSimulationData();
    }

    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationFailedException(ValidationFailedException exception) {
        return String.join("\n", exception.getErrors());
    }

}
