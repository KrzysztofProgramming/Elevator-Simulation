package me.krzysztofprogramming.elevatorsimulation.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Direction {
    UP(1),
    DOWN(-1);

    private final int value;

    public static Direction of(int value) {
        if (value == 0) throw new IllegalArgumentException("Unable to convert 0 to Direction");

        return value > 0 ? UP : DOWN;
    }

    public static Direction of(int start, int end) {
        return of(end - start);
    }
}
