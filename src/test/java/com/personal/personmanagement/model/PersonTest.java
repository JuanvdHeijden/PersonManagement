package com.personal.personmanagement.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void initPerson() {
        Person person = new Person("Max", LocalDate.now());
        Person person2 = new Person("Stef", LocalDate.now(), person, person);
        assertEquals("Max", person.getName());
        assertEquals("Stef", person2.getName());
        assertEquals("Max", person2.getParent1().getName());
    }

    @Test
    void testInitPersonWithNullValues() {
        assertThrows(NullPointerException.class, () -> new Person(null, LocalDate.now()));
        assertThrows(NullPointerException.class, () -> new Person("Max", null));
        assertThrows(NullPointerException.class, () -> new Person("Max", LocalDate.now(), null, null));
    }

}