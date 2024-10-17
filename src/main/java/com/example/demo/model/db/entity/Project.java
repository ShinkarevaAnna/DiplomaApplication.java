package com.example.demo.model.db.entity;

import com.example.demo.model.enums.ProjectsStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Column(name = "date_of_projects")
    LocalDate dateOfProjects;

    @Column(name = "object")
    String object;

    @Column(name = "name")
    String name;

    @Column(name = "comment", columnDefinition = "VARCHAR(250)")
    String comment;

    @OneToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    Invoice invoice;

    @ManyToOne
    Customer customer;

    @ManyToOne
    User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    ProjectsStatus status;

    @ManyToOne
    Guest guest;

    @ManyToMany
    @JsonManagedReference(value = "project-assistant")
    @JoinTable(name = "assistant_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "assistant_id"))
    List<Assistant> assistants;
}