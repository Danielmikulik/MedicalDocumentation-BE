package com.dm.MedicalDocumentation.person;

import com.dm.MedicalDocumentation.city.City;
import com.dm.MedicalDocumentation.city.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository repository;
    private final CityRepository cityRepository;

    public List<String> getAllPeople() {
        List<Person> people = repository.findAll();
        List<String> result = new ArrayList<>(people.size());
        for (Person person : people) {
            result.add(person.getBirthNumber() + " " + person.getFullName());
        }
        return result;
    }

    public boolean recordExists(String birthNumber) {
        Optional<Person> person = repository.findByBirthNumber(birthNumber);
        return person.isPresent();
    }

    public void createPerson(PersonRequest request) {
        City city = cityRepository.findById(request.getZipCode())
                .orElseThrow(() -> new IllegalArgumentException("No city with zipCode" + request.getZipCode() + "exists."));
        Person person = Person.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .birthDate(request.getBirthDate())
                .birthNumber(request.getBirthNumber())
                .city(city)
                .address(request.getAddress())
                .build();
        repository.save(person);
    }
}
