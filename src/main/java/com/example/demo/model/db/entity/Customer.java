package com.example.demo.model.db.entity;

import com.example.demo.model.enums.CustomerStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "customers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "name")
    String name;

    @Column(name = "phone_number")
    Long phoneNumber;

    @Column(name = "email")
    String email;

    @Column(name = "comment")
    String comment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    CustomerStatus status;


}
