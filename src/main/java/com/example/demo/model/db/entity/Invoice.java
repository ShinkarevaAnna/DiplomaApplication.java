package com.example.demo.model.db.entity;

import com.example.demo.model.enums.InvoiceStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@Table(name = "invoice")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updateAt;

    @Column(name = "project_income")
    Integer projectIncome;

    @Column(name = "reimbursable_expenses")
    Integer reimbursableExpenses;

    @Column(name = "nonReimbursable_expenses")
    Integer nonReimbursableExpenses;

    @Column(name = "assistants_salaries")
    Integer assistantsSalaries;

    @Column(name = "net_profit")
    Integer netProfit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    InvoiceStatus status;


}
