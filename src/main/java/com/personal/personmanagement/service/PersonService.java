package com.personal.personmanagement.service;

import com.personal.personmanagement.model.BasicInformation;
import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Retrieves a list of all persons.
     *
     * @return         	A list of PersonResponse objects representing all persons.
     */
    public List<PersonResponse> getAllPersons() {

        List<Person> persons = personRepository.findAll();
        List<PersonResponse> personResponse = new ArrayList<PersonResponse>();
        for(Person person : persons) {
            personResponse.add(convertToPersonResponse(person));
        }

        return personResponse;
    }

    /**
     * Retrieves a list of all persons who have a partner and three children with that partner and one
     * of the children has an age below 18
     *
     * @return         	A list of all persons that comply with the query above
     */
    public List<PersonResponse> getPersonsWithPartnerAndChildren() {
        List<Person> persons = personRepository.findAll();
        List<Person> filteredPersons = new ArrayList<Person>();
        for(Person person : persons) {
            if (person.getPartner().isPresent()) {
                Set<Person> childrenOfPerson = person.getChildren();
                Set<Person> childrenOfPartner = person.getPartner().get().getChildren();
                // Create new set with children that are also children of the partner
                Set<Person> filteredChildren = childrenOfPerson.stream().filter(childrenOfPartner::contains).collect(Collectors.toSet());
                Set<Person> filteredChildrenBelow18 = filteredChildren.stream().filter(child -> calculateAge(child.getBirthDate()) < 18).collect(Collectors.toSet());
                if(filteredChildren.size() >= 3 && !filteredChildrenBelow18.isEmpty()) {
                    filteredPersons.add(person);
                }
            }
        }
        return filteredPersons.stream().map(this::convertToPersonResponse).collect(Collectors.toList());
    }

    /**
     * Calculates the age based on the given birth date.
     *
     * @param  birthDate   the birth date of the person
     * @return             the calculated age
     */
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private PersonResponse convertToPersonResponse(Person person) {
        BasicInformation parent1 = new BasicInformation(person.getParent1().getId(), person.getParent1().getName(), person.getParent1().getBirthDate());
        BasicInformation parent2 = new BasicInformation(person.getParent2().getId(), person.getParent2().getName(), person.getParent2().getBirthDate());
        BasicInformation partner = null;
        if(person.getPartner().isPresent()) {
            partner = new BasicInformation(person.getPartner().get().getId(), person.getPartner().get().getName(), person.getPartner().get().getBirthDate());
        }
        Set<BasicInformation> children = new HashSet<>();
        for(Person child : person.getChildren()) {
            BasicInformation childInfo = new BasicInformation(child.getId(), child.getName(), child.getBirthDate());
            children.add(childInfo);
        }
        return new PersonResponse(person.getId(), person.getName(), person.getBirthDate(), parent1, parent2, children, partner);
    }
}
