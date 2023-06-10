package com.liticia.soutenanceApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder @AllArgsConstructor @NoArgsConstructor
@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_report")
    private long id;
    private String name;
    private String note;
    private String levelOfVisibility;
    private Date createdAt;
}
