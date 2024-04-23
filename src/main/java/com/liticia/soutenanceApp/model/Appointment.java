package com.liticia.soutenanceApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.util.Date;

@Builder @AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "availabilityId", referencedColumnName = "id")
    private Availability availability;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "report", referencedColumnName = "id")
    private Report report;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "reportPro", referencedColumnName = "id")
    private Report reportPro;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userPro", referencedColumnName = "id")
    private User userPro;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userCustomer", referencedColumnName = "id")
    private User userCustomer;

    private String pattern;

    private String description;

    private String document;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Instant createdAt;

    private boolean deleted = false;
}
