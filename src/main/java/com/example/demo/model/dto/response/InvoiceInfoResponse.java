package com.example.demo.model.dto.response;

import com.example.demo.model.dto.request.InvoiceInfoRequest;
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
public class InvoiceInfoResponse extends InvoiceInfoRequest {
    Long id;
}
