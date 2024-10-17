package com.example.demo.controllers;

import com.example.demo.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/downloadProject/{projectId}")
    @Operation(summary = "download excel project")
    public void downloadProjectByIdAsExcel(@PathVariable Long projectId, HttpServletResponse response) throws IOException {
        excelService.downloadProjectByIdAsExcel(projectId, response);
    }
}
