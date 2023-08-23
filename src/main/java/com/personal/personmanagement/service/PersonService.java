package com.personal.personmanagement.service;

import com.personal.personmanagement.model.BasicInformation;
import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.model.PersonRequest;
import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.repository.PersonRepository;
import com.personal.personmanagement.utils.ResponseConverter;
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
    public String getPersonsWithPartnerAndChildren() {
        List<Person> persons = personRepository.findAll();
        List<Person> filteredPersons = filterOnPartnerAndChildren(persons);
        List<PersonResponse> personResponse = filteredPersons.stream().map(this::convertToPersonResponse).toList();
        ResponseConverter responseConverter = new ResponseConverter();
        return responseConverter.encodeToBase64(responseConverter.convertToCsv(personResponse));
    }

    public List<Person> filterOnPartnerAndChildren(List<Person> persons) {
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
        return filteredPersons;
    }

    /**
     * Creates a new person using the provided person request.
     *
     * @param  personRequest  the person request object containing the necessary information to create a person
     * @return                the newly created person
     */
    public Person createPerson(PersonRequest personRequest) {
        Person parent1 = personRepository.findById(personRequest.getParent1Id())
                .orElseThrow(() -> new IllegalArgumentException("Parent 1 with ID " + personRequest.getParent1Id() + " not found"));

        Person parent2 = personRepository.findById(personRequest.getParent2Id())
                .orElseThrow(() -> new IllegalArgumentException("Parent 2 with ID " + personRequest.getParent2Id() + " not found"));

        Person partner = null;
        if (personRequest.getPartnerId() != null) {
            partner = personRepository.findById(personRequest.getPartnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Partner with ID " + personRequest.getPartnerId() + " not found"));
        }

        Person newPerson = new Person(personRequest.getName(), personRequest.getBirthDate(), parent1, parent2);
        newPerson.setPartner(partner);

        for (Long childId : personRequest.getChildrenIds()) {
            Person child = personRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("Child with ID " + childId + " not found"));
            newPerson.addChild(child);
        }

        return personRepository.save(newPerson);
    }

    /**
     * Deletes a person and updates related relationships accordingly.
     *
     * @param personId   The ID of the person to be deleted.
     */
    public void deletePerson(Long personId) {
        Person personToDelete = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person with ID " + personId + " not found"));

        // Remove the person from parent1's children if applicable
        Person parent1 = personToDelete.getParent1();
        if (parent1 != null) {
            parent1.getChildren().remove(personToDelete);
        }

        // Remove the person from parent2's children if applicable
        Person parent2 = personToDelete.getParent2();
        if (parent2 != null) {
            parent2.getChildren().remove(personToDelete);
        }

        // Update partner's relationship if applicable
        Person partner = personToDelete.getPartner().orElse(null);
        if (partner != null) {
            partner.setPartner(null);
        }

        // Delete the person
        personRepository.delete(personToDelete);
    }

    public PersonResponse updatePerson(Long personId, PersonRequest personRequest) {
        Person personToUpdate = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person with ID " + personId + " not found"));

        personToUpdate.setName(personRequest.getName());
        personToUpdate.setBirthDate(personRequest.getBirthDate());

        // Update parent1 and parent2 relationships if applicable
        Person newParent1 = personRepository.findById(personRequest.getParent1Id())
                .orElseThrow(() -> new IllegalArgumentException("Parent 1 with ID " + personRequest.getParent1Id() + " not found"));
        personToUpdate.setParent1(newParent1);

        Person newParent2 = personRepository.findById(personRequest.getParent2Id())
                .orElseThrow(() -> new IllegalArgumentException("Parent 2 with ID " + personRequest.getParent2Id() + " not found"));
        personToUpdate.setParent2(newParent2);

        // Update partner relationship if applicable
        if (personRequest.getPartnerId() != null) {
            Person newPartner = personRepository.findById(personRequest.getPartnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Partner with ID " + personRequest.getPartnerId() + " not found"));
            personToUpdate.setPartner(newPartner);
        } else {
            personToUpdate.setPartner(null);
        }

        // Update children relationships if applicable
        if (personRequest.getChildrenIds() != null) {
            Set<Person> newChildren = personRequest.getChildrenIds().stream()
                    .map(childId -> personRepository.findById(childId)
                            .orElseThrow(() -> new IllegalArgumentException("Child with ID " + childId + " not found")))
                    .collect(Collectors.toSet());
            personToUpdate.setChildren(newChildren);
        }

        personRepository.save(personToUpdate);

        return convertToPersonResponse(personToUpdate);
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
