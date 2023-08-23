package com.personal.personmanagement.controller;

import com.personal.personmanagement.model.BasicInformation;
import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.model.PersonRequest;
import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.service.PersonService;
import com.personal.personmanagement.utils.ResponseConverter;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/filter")
    public ResponseEntity<String> getPersonsWithPartnerAndChildren() {
        try {
            return ResponseEntity.ok(personService.getPersonsWithPartnerAndChildren());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path= "addPerson",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> addPerson(@RequestBody PersonRequest personRequest) {
        try {
            personService.createPerson(personRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Person created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.ok("Person deleted successfully");
        } catch (Exception e) {
            String errorMessage = "Person NOT deleted, this probably has to do with cascading issues, make sure all related children are deleted first. ID " + id;
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/update/{personId}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long personId, @RequestBody PersonRequest personRequest) {
        try {
            PersonResponse updatedPersonResponse = personService.updatePerson(personId, personRequest);
            return ResponseEntity.ok(updatedPersonResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

}
