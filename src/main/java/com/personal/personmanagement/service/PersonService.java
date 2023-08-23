package com.personal.personmanagement.service;

import com.personal.personmanagement.model.BasicInformation;
import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonResponse> getAllPersons() {

        List<Person> persons = personRepository.findAll();
        List<PersonResponse> personResponse = new ArrayList<PersonResponse>();
        for(Person person : persons) {
            personResponse.add(convertToPersonResponse(person));
        }

        return personResponse;
    }

    private PersonResponse convertToPersonResponse(Person person) {
        BasicInformation parent1 = new BasicInformation(person.getParent1().getId(), person.getParent1().getName(), person.getParent1().getBirthDate());
        BasicInformation parent2 = new BasicInformation(person.getParent2().getId(), person.getParent2().getName(), person.getParent2().getBirthDate());
        BasicInformation partner = new BasicInformation(person.getPartner().getId(), person.getPartner().getName(), person.getPartner().getBirthDate());
        Set<BasicInformation> children = new HashSet<>();
        for(Person child : person.getChildren()) {
            BasicInformation childInfo = new BasicInformation(child.getId(), child.getName(), child.getBirthDate());
            children.add(childInfo);
        }
        return new PersonResponse(person.getId(), person.getName(), person.getBirthDate(), parent1, parent2, children, partner);
    }
}
