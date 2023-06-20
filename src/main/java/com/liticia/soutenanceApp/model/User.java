package com.liticia.soutenanceApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Builder @AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String passWord;
    private String document;
    private String address;
    private String details;
    private String image;
    private int phone;
    private Date createdAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "speciality", referencedColumnName = "id_speciality")
    private Speciality speciality;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "city", referencedColumnName = "id_city")
    private City city;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles = new HashSet<>();
}
