package com.example.demo.service;

import com.example.demo.model.db.entity.*;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.dto.response.ReportFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelService {
    private final ObjectMapper mapper;
    private final ProjectsService projectsService;
    private final InvoiceService invoiceService;
    private final AssistantService assistantService;
    private final CustomerService customerService;
    private final UserService userService;

    public ReportFile downloadProjectByIdAsExcel(Long id) throws IOException {
        Project project = projectsService.getProjectById(id);

        Invoice invoice = mapper.convertValue(projectsService.getProjectInvoice(id), Invoice.class);
        User user = project.getUser();
        List<Assistant> assistants = assistantService.getProjectAssistantsWithoutPagination(id);
        Customer customer = project.getCustomer();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(project.getName());

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("first_name");
        row.createCell(1).setCellValue("last_name");
        row.createCell(2).setCellValue("middle_name");
        row.createCell(3).setCellValue("Project_Name");
        row.createCell(4).setCellValue("date_of_projects");
        row.createCell(5).setCellValue("object");
        row.createCell(6).setCellValue("comment_Project");
        row.createCell(7).setCellValue("project_Income");
        row.createCell(8).setCellValue("reimbursable_Expenses");
        row.createCell(9).setCellValue("nonReimbursable_Expenses");
        row.createCell(10).setCellValue("assistants_Salaries");
        row.createCell(11).setCellValue("net_Profit");
        row.createCell(12).setCellValue("name_Customer");
        row.createCell(13).setCellValue("phoneNumber_Customer");
        row.createCell(14).setCellValue("email_Customer");
        row.createCell(15).setCellValue("comment_Customer");
        row.createCell(16).setCellValue("first_name_assistant");
        row.createCell(17).setCellValue("last_name_assistant");
        row.createCell(18).setCellValue("middle_name_assistant");
        row.createCell(19).setCellValue("phoneNumber_Assistant");
        row.createCell(20).setCellValue("email_Assistant");
        row.createCell(21).setCellValue("comment_Assistant");

    Row dataRow = sheet.createRow(1);
    dataRow.createCell(0).setCellValue(user.getFirstName() != null ? user.getFirstName() : "null");
    dataRow.createCell(1).setCellValue(user.getLastName() != null ? user.getLastName() : "null");
    dataRow.createCell(2).setCellValue(user.getMiddleName() != null ? user.getMiddleName() : "null");
    dataRow.createCell(3).setCellValue(project.getName() != null ? project.getName() : "null");
    dataRow.createCell(4).setCellValue(project.getDateOfProjects());
    dataRow.createCell(5).setCellValue(project.getObject() != null ? project.getObject() : "null");
    dataRow.createCell(6).setCellValue(project.getComment() != null ? project.getComment() : "null");
    dataRow.createCell(7).setCellValue(invoice.getProjectIncome() != null ? invoice.getProjectIncome() : 0);
    dataRow.createCell(8).setCellValue(invoice.getReimbursableExpenses() != null ? invoice.getReimbursableExpenses() : 0);
    dataRow.createCell(9).setCellValue(invoice.getNonReimbursableExpenses() != null ? invoice.getNonReimbursableExpenses() : 0);
    dataRow.createCell(10).setCellValue(invoice.getAssistantsSalaries() != null ? invoice.getAssistantsSalaries() : 0);
    dataRow.createCell(11).setCellValue(invoice.getNetProfit() != null ? invoice.getNetProfit() : 0);
    dataRow.createCell(12).setCellValue(customer.getName() != null ? customer.getName() : "null");
    dataRow.createCell(13).setCellValue(customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "null");
    dataRow.createCell(14).setCellValue(customer.getEmail() != null ? customer.getEmail() : "null");
    dataRow.createCell(15).setCellValue(customer.getComment() != null ? customer.getComment() : "null");
    for (int i = 0; i<assistants.size(); i++) {
        dataRow.createCell(16).setCellValue(assistants.get(i).getFirstName() != null ? assistants.get(i).getFirstName() : "null");
        dataRow.createCell(17).setCellValue(assistants.get(i).getLastName() != null ? assistants.get(i).getLastName() : "null");
        dataRow.createCell(18).setCellValue(assistants.get(i).getMiddleName() != null ? assistants.get(i).getMiddleName() : "null");
        dataRow.createCell(19).setCellValue(assistants.get(i).getPhoneNumber() != null ? assistants.get(i).getPhoneNumber() : "null");
        dataRow.createCell(20).setCellValue(assistants.get(i).getEmail() != null ? assistants.get(i).getEmail() : "null");
        dataRow.createCell(21).setCellValue(assistants.get(i).getComment() != null ? assistants.get(i).getComment() : "null");
        dataRow = sheet.createRow(i+2);
    }

//    Row dataRow1 = sheet.createRow(2);
//    dataRow1.createCell(16).setCellValue(assistants.get(1).getFirstName() != null ? assistants.get(1).getFirstName() : "null");
//    dataRow1.createCell(17).setCellValue(assistants.get(1).getLastName() != null ? assistants.get(1).getLastName() : "null");
//    dataRow1.createCell(18).setCellValue(assistants.get(1).getMiddleName() != null ? assistants.get(1).getMiddleName() : "null");
//    dataRow1.createCell(19).setCellValue(assistants.get(1).getPhoneNumber() != null ? assistants.get(1).getPhoneNumber() :"null");
//    dataRow1.createCell(20).setCellValue(assistants.get(1).getEmail() != null ? assistants.get(1).getEmail() : "null");
//    dataRow1.createCell(21).setCellValue(assistants.get(1).getComment() != null ? assistants.get(1).getComment() : "null");
//
//
//    Row dataRow2 = sheet.createRow(3);
//    dataRow2.createCell(16).setCellValue(assistants.get(2).getFirstName() != null ? assistants.get(2).getFirstName() : "null");
//    dataRow2.createCell(17).setCellValue(assistants.get(2).getLastName() != null ? assistants.get(2).getLastName() : "null");
//    dataRow2.createCell(18).setCellValue(assistants.get(2).getMiddleName() != null ? assistants.get(2).getMiddleName() : "null");
//    dataRow2.createCell(19).setCellValue(assistants.get(2).getPhoneNumber() != null ? assistants.get(2).getPhoneNumber() :"null");
//    dataRow2.createCell(20).setCellValue(assistants.get(2).getEmail() != null ? assistants.get(2).getEmail() : "null");
//    dataRow2.createCell(21).setCellValue(assistants.get(2).getComment() != null ? assistants.get(2).getComment() : "null");
//
//    Row dataRow3 = sheet.createRow(4);
//    dataRow3.createCell(16).setCellValue(assistants.get(3).getFirstName() != null ? assistants.get(3).getFirstName() : "null");
//    dataRow3.createCell(17).setCellValue(assistants.get(3).getLastName() != null ? assistants.get(3).getLastName() : "null");
//    dataRow3.createCell(18).setCellValue(assistants.get(3).getMiddleName() != null ? assistants.get(3).getMiddleName() : "null");
//    dataRow3.createCell(19).setCellValue(assistants.get(3).getPhoneNumber() != null ? assistants.get(3).getPhoneNumber() : "null");
//    dataRow3.createCell(20).setCellValue(assistants.get(3).getEmail() != null ? assistants.get(3).getEmail() : "null");
//    dataRow3.createCell(21).setCellValue(assistants.get(3).getComment() != null ? assistants.get(3).getComment() : "null");
//
//
//    Row dataRow4 = sheet.createRow(5);
//    dataRow4.createCell(16).setCellValue(assistants.get(4).getFirstName() != null ? assistants.get(4).getFirstName() : "null");
//    dataRow4.createCell(17).setCellValue(assistants.get(4).getLastName() != null ? assistants.get(4).getLastName() : "null");
//    dataRow4.createCell(18).setCellValue(assistants.get(4).getMiddleName() != null ? assistants.get(4).getMiddleName() : "null");
//    dataRow4.createCell(19).setCellValue(assistants.get(4).getPhoneNumber() != null ? assistants.get(4).getPhoneNumber() :"null");
//    dataRow4.createCell(20).setCellValue(assistants.get(4).getEmail() != null ? assistants.get(4).getEmail() : "null");
//    dataRow4.createCell(21).setCellValue(assistants.get(4).getComment() != null ? assistants.get(4).getComment() : "null");

//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename= project_" + project.getName() + ".xlsx");
//        workbook.write(response.getOutputStream());
//        workbook.close();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       workbook.write(byteArrayOutputStream);
        return ReportFile.builder()
                .content(byteArrayOutputStream.toByteArray())
                .fileName(project.getName())
                .build();
    }
}
