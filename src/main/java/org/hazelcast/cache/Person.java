package org.hazelcast.cache;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

public class Person implements Serializable {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}