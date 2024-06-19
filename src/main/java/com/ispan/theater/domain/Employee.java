package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "dept_id", nullable = false)
    private Integer deptId;

    @Column(name = "\"position\"", nullable = false, length = 20)
    private String position;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @Column(name = "hiredate", nullable = false)
    private LocalDate hiredate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "\"level\"", nullable = false)
    private Integer level;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "personal_phone", nullable = false, length = 20)
    private String personalPhone;

    @Column(name = "company_phone", length = 20)
    private String companyPhone;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    @Column(name = "Education", length = 50)
    private String education;

    @Column(name = "National_id", nullable = false, length = 20)
    private String nationalId;

}