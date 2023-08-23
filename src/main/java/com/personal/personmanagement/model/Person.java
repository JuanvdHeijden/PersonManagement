package com.personal.personmanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity(name = "Person")
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "birth_date", nullable = false)
    @NonNull
    private LocalDate birthDate;

    @ManyToOne
    @NonNull
    private Person parent1;

    @ManyToOne
    @NonNull
    private Person parent2;

    @ManyToMany
    private Set<Person> children;

    @ManyToOne
    private Person partner;

    public Optional<Person> getPartner() {
        return Optional.ofNullable(partner);
    }

    public Person(@NonNull String name, @NonNull LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = this;
        this.parent2 = this;
        this.partner = this;
        this.children = new HashSet<Person>();
    }

    public Person(@NonNull String name, @NonNull LocalDate birthDate, @NonNull Person parent1, @NonNull Person parent2) {
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.children = new HashSet<Person>();
        this.partner = null;
    }

    public Person(@NonNull String name, @NonNull LocalDate birthDate, @NonNull Person parent1, @NonNull Person parent2, Set<Person> children) {
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.children = children;
        this.partner = null;
    }
    public Person(@NonNull String name, @NonNull LocalDate birthDate, @NonNull Person parent1, @NonNull Person parent2, Set<Person> children, Person partner) {
        this.name = name;
        this.birthDate = birthDate;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.children = children;
        this.partner = partner;
    }

    protected Person(){

    }

    public void addChild(Person child1) {
        this.children.add(child1);
    }
}