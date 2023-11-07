package com.example.bootIML.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "iml_param")
public class ImlParam {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "prog_name")
    private String progName;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_val")
    private Integer paramVal;
}

