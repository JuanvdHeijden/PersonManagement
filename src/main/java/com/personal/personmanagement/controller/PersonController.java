package com.personal.personmanagement.controller;

import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAll() {
        try {
            return ResponseEntity.ok(personService.getAllPersons());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
