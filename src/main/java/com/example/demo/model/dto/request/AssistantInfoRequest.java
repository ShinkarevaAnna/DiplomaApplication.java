package com.example.demo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties
public class AssistantInfoRequest {

    @NotEmpty
    String firstName;
    String lastName;
    String middleName;
    Long phoneNumber;
    String email;
    String comment;
}
