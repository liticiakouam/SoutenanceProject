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
public class Disponibility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_disponibility")
    private long id;
    private long idUser;
    private Date date;
    private Date createdAt;
    private Time time;
}
