package com.personal.personmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Getter
public class PersonResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @JsonProperty("parent1")
    private BasicInformation parent1;
    @JsonProperty("parent2")
    private BasicInformation parent2;
    @JsonProperty("partner")
    private BasicInformation partner;

    public Optional<BasicInformation> getPartner() {
        return Optional.ofNullable(partner);
    }

    public PersonResponse(Long id, String name, LocalDate birthDate, BasicInformation parent1, BasicInformation parent2, Set<BasicInformation> children, BasicInformation partner) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.partner = partner;
    }

}