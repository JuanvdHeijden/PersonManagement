package com.personal.personmanagement.service;

import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.model.PersonResponse;
import com.personal.personmanagement.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(personRepository);
    }

    @Test
    void testGetAllPersons() {
        // Create mock objects

        // Create test data
        List<Person> mockPersons = new ArrayList<>();
        mockPersons.add(new Person("John", LocalDate.of(1980, 1, 1)));
        mockPersons.add(new Person("Alice", LocalDate.of(1985, 2, 2)));

        // Mock the behavior of the personRepository
        when(personRepository.findAll()).thenReturn(mockPersons);

        // Call the function
        List<PersonResponse> result = personService.getAllPersons("");

        // Check the result
        assertEquals(mockPersons.size(), result.size());
        for (int i = 0; i < mockPersons.size(); i++) {
            assertEquals(mockPersons.get(i).getName(), result.get(i).getName());
            assertEquals(mockPersons.get(i).getBirthDate(), result.get(i).getBirthDate());
        }
    }

    @Test
    void testGetPersonsWithPartnerAndChildren() {
        // Create test persons with partners and children
        Person person1 = new Person("John", LocalDate.of(1980, 1, 1));
        Person person2 = new Person("Alice", LocalDate.of(1985, 2, 2));
        Person person3 = new Person("Bob", LocalDate.of(1990, 3, 3));
        Person partner1 = new Person("Partner1", LocalDate.of(1982, 4, 4));
        Person partner2 = new Person("Partner2", LocalDate.of(1987, 5, 5));
        Person partner3 = new Person("Partner3", LocalDate.of(1992, 6, 6));
        Person child1 = new Person("Child1", LocalDate.of(1996, 7, 7));
        Person child2 = new Person("Child2", LocalDate.of(1996, 8, 8));
        Person child3 = new Person("Child3", LocalDate.of(2012, 9, 9));
        Person child4 = new Person("Child4", LocalDate.of(1996, 10, 10));
        Person child5 = new Person("Child5", LocalDate.of(1996, 11, 11));

        // Person that has 3 children with partner 1 of which one is below 18
        person1.setPartner(partner1);
        person1.addChild(child1);
        person1.addChild(child2);
        person1.addChild(child3);
        partner1.addChild(child1);
        partner1.addChild(child2);
        partner1.addChild(child3);

        // Person that has 3 children 1 of which one is below 18, however one is from another partner
        person2.setPartner(partner2);
        person2.addChild(child2);
        person2.addChild(child3);
        person2.addChild(child4);
        partner2.addChild(child2);
        partner2.addChild(child3);

        // Person that has 3 children with partner 1 but none are below 18
        person3.setPartner(partner3);
        person3.addChild(child2);
        person3.addChild(child4);
        person3.addChild(child5);
        partner3.addChild(child2);
        partner3.addChild(child4);
        partner3.addChild(child5);

        List<Person> persons = Arrays.asList(person1, person2, person3, partner1, partner2, partner3, child1, child2, child3, child4, child5);

        // Mock personRepository to return the test persons
        when(personRepository.findAll()).thenReturn(persons);

        // Call the function
        String result = personService.getPersonsWithPartnerAndChildren();

        // Id's are null since they are not generated
        String expectedResult = "SWQsTmFtZSxCaXJ0aERhdGUsUGFyZW50MSxQYXJlbnQyLFBhcnRuZXIKbnVsbCxKb2huLDE5ODAtMDEtMDEsbnVsbCxudWxsLG51bGwKbnVsbCxQYXJ0bmVyMSwxOTgyLTA0LTA0LG51bGwsbnVsbCxudWxs";
        assertEquals(expectedResult, result);

    }

    @Test
    void testGetPersonsWithPartnerAndChildren_NoMatch() {
        // Create test persons without partners and children
        Person person1 = new Person("John", LocalDate.of(1980, 1, 1));
        Person person2 = new Person("Alice", LocalDate.of(1985, 2, 2));
        Person person3 = new Person("Bob", LocalDate.of(1990, 3, 3));

        List<Person> persons = Arrays.asList(person1, person2, person3);

        // Mock personRepository to return the test persons
        when(personRepository.findAll()).thenReturn(persons);

        // Call the function
        String result = personService.getPersonsWithPartnerAndChildren();
        // Base64 of only headers
        String expectedResult = "SWQsTmFtZSxCaXJ0aERhdGUsUGFyZW50MSxQYXJlbnQyLFBhcnRuZXIK";
        assertEquals(expectedResult, result);
    }

}