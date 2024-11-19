package com.example.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportFile {

    private byte[] content;
    private String fileName;

}

