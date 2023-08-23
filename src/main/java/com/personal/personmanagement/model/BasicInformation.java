package com.personal.personmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BasicInformation {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    public BasicInformation(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }
}
