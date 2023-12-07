package com.liticia.soutenanceApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String domain;
    private String passWord;
    private String document;
    private String address;
    private String details;
    private String image;
    private int phone;
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "speciality", referencedColumnName = "id")
    private Speciality speciality;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "city", referencedColumnName = "id")
    private City city;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();
}
    