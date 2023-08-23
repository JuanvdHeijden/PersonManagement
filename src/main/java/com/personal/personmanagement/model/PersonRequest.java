package com.personal.personmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PersonRequest {
    private String name;
    private LocalDate birthDate;
    private Long parent1Id;
    private Long parent2Id;
    private Long partnerId;
    private List<Long> childrenIds;

}