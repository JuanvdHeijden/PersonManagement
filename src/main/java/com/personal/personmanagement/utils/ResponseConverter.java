package com.personal.personmanagement.utils;

import com.personal.personmanagement.model.PersonResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseConverter {
    public String convertToCsv(List<PersonResponse> persons) {
        String header = "Id,Name,BirthDate,Parent1,Parent2,Partner\n";
        String csvData = persons.stream()
                .map(this::personToCsv)
                .collect(Collectors.joining("\n"));

        return header + csvData;
    }

    private String personToCsv(PersonResponse person) {
        return String.format("%d,%s,%s,%s,%s,%s",
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                person.getParent1().getId(),
                person.getParent2().getId(),
                person.getPartner().isPresent() ? person.getPartner().get().getId() : ""
        );
    }

    public String encodeToBase64(String data) {
        byte[] encodedBytes = java.util.Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}
