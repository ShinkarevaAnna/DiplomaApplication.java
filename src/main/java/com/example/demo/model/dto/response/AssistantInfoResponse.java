package com.example.demo.model.dto.response;

import com.example.demo.model.dto.request.AssistantInfoRequest;
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
@JsonIgnoreProperties
@EqualsAndHashCode
public class AssistantInfoResponse extends AssistantInfoRequest {
    Long id;
}
