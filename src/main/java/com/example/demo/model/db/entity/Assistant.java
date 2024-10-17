package com.example.demo.model.db.entity;

import com.example.demo.model.enums.AssistantStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "assistants")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "phone_number")
    Long phoneNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    AssistantStatus status;

    @Column(name = "email")
    String email;

    @Column(name = "comment", columnDefinition = "VARCHAR(250)")
    String comment;

    @ManyToMany(mappedBy = "assistants")
    @JsonBackReference(value = "project-assistant")
    List<Project> projects;
}


