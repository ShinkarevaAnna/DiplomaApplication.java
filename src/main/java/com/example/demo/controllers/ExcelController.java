package com.example.demo.controllers;

import com.example.demo.model.dto.response.ReportFile;
import com.example.demo.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.demo.constants.Constants.EXCEL;

@Tag(name = "ExcelController")
@RestController
@RequestMapping(EXCEL)
@RequiredArgsConstructor
public class ExcelController {
private final ExcelService excelService;

    private static ResponseEntity<Resource> getFileResponse(ReportFile reportFile) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportFile.getFileName() + ".xlsx")
                .body(new ByteArrayResource(reportFile.getContent()));
    }

    @GetMapping(value = "/downloadProject/{projectId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "download excel project")
    public ResponseEntity<Resource> downloadProjectByIdAsExcel(@PathVariable Long projectId) throws IOException {

       return getFileResponse(excelService.downloadProjectByIdAsExcel(projectId));

    }
}
