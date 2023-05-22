package me.krzysztofprogramming.elevatorsimulation.app;

import me.krzysztofprogramming.elevatorsimulation.app.model.Direction;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorRequest;
import me.krzysztofprogramming.elevatorsimulation.app.model.ElevatorTarget;
import me.krzysztofprogramming.elevatorsimulation.app.model.TargetType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ElevatorTest {


    static Stream<Arguments> shouldAddNewReleaseTarget() {
        ElevatorSystem elevatorSystem = new ElevatorSystem(6, 3, 20, 0);
        return Stream.of(
                Arguments.of(
                        new Elevator(1, 3, elevatorSystem, List.of(
                                new ElevatorTarget(4, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(6, Direction.UP, TargetType.PICKUP),
                                new ElevatorTarget(9, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(11, Direction.DOWN, TargetType.PICKUP)
                        )),
                        List.of(
                                new ElevatorTarget(4, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(6, Direction.UP, TargetType.PICKUP),
                                new ElevatorTarget(9, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(13, Direction.DOWN, TargetType.RELEASE),
                                new ElevatorTarget(11, Direction.DOWN, TargetType.PICKUP)
                        ), 13
                ),
                Arguments.of(
                        new Elevator(1, 15, elevatorSystem, List.of(
                                new ElevatorTarget(13, Direction.DOWN, TargetType.RELEASE),
                                new ElevatorTarget(9, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, null, TargetType.RELEASE)
                        )),
                        List.of(
                                new ElevatorTarget(13, Direction.DOWN, TargetType.RELEASE),
                                new ElevatorTarget(9, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.RELEASE),
                                new ElevatorTarget(0, null, TargetType.RELEASE)
                        ), 4
                )
        );
    }

    static Stream<Arguments> shouldAddNewRequest() {
        ElevatorSystem elevatorSystem = new ElevatorSystem(6, 3, 20, 0);
        return Stream.of(
                Arguments.of(
                        new Elevator(1, 16, elevatorSystem, List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, null, TargetType.RELEASE)
                        )),
                        List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(1, Direction.UP, TargetType.PICKUP)
                        ), new ElevatorRequest(1, Direction.UP)
                ),
                Arguments.of(
                        new Elevator(1, 16, elevatorSystem, List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, null, TargetType.RELEASE)
                        )),
                        List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(5, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, null, TargetType.RELEASE)
                        ), new ElevatorRequest(5, Direction.DOWN)),
                Arguments.of(
                        new Elevator(1, 16, elevatorSystem, List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(18, Direction.DOWN, TargetType.PICKUP)
                        )),
                        List.of(
                                new ElevatorTarget(15, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(8, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(4, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(0, Direction.UP, TargetType.RELEASE),
                                new ElevatorTarget(18, Direction.DOWN, TargetType.PICKUP),
                                new ElevatorTarget(17, Direction.DOWN, TargetType.PICKUP)
                        ), new ElevatorRequest(17, Direction.DOWN)),
                Arguments.of(
                        new Elevator(1, 0, elevatorSystem, List.of(
                                new ElevatorTarget(10, Direction.UP, TargetType.PICKUP)
                        )),
                        List.of(
                                new ElevatorTarget(5, Direction.UP, TargetType.PICKUP),
                                new ElevatorTarget(10, Direction.UP, TargetType.PICKUP)
                        ), new ElevatorRequest(5, Direction.UP))
        );
    }

    @ParameterizedTest()
    @MethodSource
    public void shouldAddNewReleaseTarget(Elevator elevator, List<ElevatorTarget> expectedTarget, int floor) {
        //given
        //when
        elevator.addNewReleaseTarget(floor);

        //then
        assertThat(elevator).returns(expectedTarget, Elevator::getTargetFloors);
    }

    @Test
    public void shouldCalculateWaitingTime() {
        //given
        ElevatorSystem elevatorSystem = new ElevatorSystem(6, 2, 20, 0);
        Elevator elevator = new Elevator(1, 4, List.of(5, 7, 8, 10, 3, 1), elevatorSystem);
        Elevator elevator2 = new Elevator(2, 3, List.of(4, 6, 10), elevatorSystem, Direction.UP);
        ElevatorRequest request1 = new ElevatorRequest(3, Direction.UP);
        ElevatorRequest request2 = new ElevatorRequest(5, Direction.DOWN);
        ElevatorRequest request3 = new ElevatorRequest(4, Direction.DOWN);
        ElevatorRequest request4 = new ElevatorRequest(4, Direction.UP);
        ElevatorRequest request5 = new ElevatorRequest(9, Direction.DOWN);
        ElevatorRequest request6 = new ElevatorRequest(11, Direction.UP);
        ElevatorRequest request7 = new ElevatorRequest(11, Direction.DOWN);

        //when then
        Assertions.assertEquals(23, elevator.estimateWaitingCost(request1));
        Assertions.assertEquals(3, elevator.estimateWaitingCost(request2));
        Assertions.assertEquals(22, elevator.estimateWaitingCost(request3));
        Assertions.assertEquals(32, elevator.estimateWaitingCost(request4));
        Assertions.assertEquals(17, elevator.estimateWaitingCost(request5));
        Assertions.assertEquals(16, elevator2.estimateWaitingCost(request6));
        Assertions.assertEquals(35, elevator2.estimateWaitingCost(request7));
    }

    @ParameterizedTest()
    @MethodSource
    public void shouldAddNewRequest(Elevator elevator, List<ElevatorTarget> expectedTarget, ElevatorRequest request) {
        //given
        //when
        elevator.addNewRequest(request);

        //then
        assertThat(elevator).returns(expectedTarget, Elevator::getTargetFloors);
    }
}
