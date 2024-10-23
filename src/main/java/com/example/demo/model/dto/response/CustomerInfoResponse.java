package com.example.demo.model.dto.response;

import com.example.demo.model.dto.request.CustomerInfoRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class CustomerInfoResponse extends CustomerInfoRequest {
    Long id;
}
