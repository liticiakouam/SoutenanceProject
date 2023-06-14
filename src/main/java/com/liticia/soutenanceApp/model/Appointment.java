package com.liticia.soutenanceApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Builder @AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_appointment")
    private long id;
    private long idDisponibility;
    private long idReport;
    private long idUser;
    private String pattern;
    private String document;
    private Date createdAt;
}
