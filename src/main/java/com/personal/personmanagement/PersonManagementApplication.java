package com.personal.personmanagement;

import com.personal.personmanagement.model.Person;
import com.personal.personmanagement.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PersonManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner insertSamplePersons(PersonRepository personRepository) {
        return args -> {
            Person firstPerson = new Person("FirstPerson", LocalDate.now());
            Person father1 = new Person("Father1", LocalDate.now(), firstPerson, firstPerson, new HashSet<>());
            Person mother1 = new Person("Mother1", LocalDate.now(), firstPerson, firstPerson, new HashSet<>(), father1);
            Person father2 = new Person("Father2", LocalDate.now(), firstPerson, firstPerson, new HashSet<>());
            Person mother2 = new Person("Mother2", LocalDate.now(), firstPerson, firstPerson, new HashSet<>(), father2);
            father1.setPartner(mother1);
            father2.setPartner(mother2);
            Person child1 = new Person("Child1", LocalDate.of(1980, 1, 1), father1, mother1, new HashSet<>());
            Person child2 = new Person("Child2", LocalDate.of(2015, 1, 1), father1, mother1, new HashSet<>(), child1);
            Person child3 = new Person("Child3", LocalDate.of(1980, 1, 1), father1, mother1, new HashSet<>(), child2);
            Person child4 = new Person("Child4", LocalDate.now(), father2, mother2, new HashSet<>(), child1);
            child1.setPartner(child4);

            Set<Person> firstChildrenList = new HashSet<>();
            firstChildrenList.add(child1);
            firstChildrenList.add(child2);
            firstChildrenList.add(child3);
            father1.setChildren(firstChildrenList);
            mother1.setChildren(firstChildrenList);
            Set<Person> secondChildrenList = new HashSet<>();
            secondChildrenList.add(child4);
            father2.setChildren(secondChildrenList);
            mother2.setChildren(secondChildrenList);


            personRepository.saveAll(
                    new HashSet<>(Arrays.asList(firstPerson, father1, mother1, father2, mother2, child1, child2, child3, child4))
            );
        };
    }
}
