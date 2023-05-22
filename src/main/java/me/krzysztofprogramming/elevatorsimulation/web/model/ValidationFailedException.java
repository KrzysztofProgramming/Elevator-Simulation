package me.krzysztofprogramming.elevatorsimulation.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ValidationFailedException extends RuntimeException {
    private List<String> errors;
}
