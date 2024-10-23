package com.example.demo.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportFile {

    /**
     * Содержимое отчета
     */
    private byte[] content;

    /**
     * Имя файла
     */
    private String fileName;

}

