package com.example.demo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceInfoRequest {
    @NotNull
    Integer projectIncome;
    Integer reimbursableExpenses;
    @NotNull
    Integer nonReimbursableExpenses;
    @NotNull
    Integer assistantsSalaries;
    Integer netProfit = projectIncome - nonReimbursableExpenses - assistantsSalaries;
}
