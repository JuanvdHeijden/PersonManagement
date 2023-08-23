package com.personal.personmanagement.repository;

import com.personal.personmanagement.model.Person;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person,Long> {
    @NonNull List<Person> findAll();

}
