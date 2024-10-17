package com.example.demo.service;

import com.example.demo.model.db.entity.*;
import com.example.demo.model.db.repository.ProjectRepository;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.dto.response.ProjectInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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

    public void downloadProjectByIdAsExcel(Long id, HttpServletResponse response) throws IOException {
        Project project = mapper.convertValue(projectsService.getProject(id), Project.class);
        Invoice invoice = mapper.convertValue(invoiceService.getProjectInvoice(id), Invoice.class);
        User user = project.getUser();
        List<AssistantInfoResponse> assistants = assistantService.getProjectAssistantsWithoutPagination(id);
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
        dataRow.createCell(0).setCellValue(user.getFirstName());
        dataRow.createCell(1).setCellValue(user.getLastName());
        dataRow.createCell(2).setCellValue(user.getMiddleName());
        dataRow.createCell(3).setCellValue(project.getName());
        dataRow.createCell(4).setCellValue(project.getDateOfProjects());
        dataRow.createCell(5).setCellValue(project.getObject());
        dataRow.createCell(6).setCellValue(project.getComment());
        dataRow.createCell(7).setCellValue(invoice.getProjectIncome());
        dataRow.createCell(8).setCellValue(invoice.getReimbursableExpenses());
        dataRow.createCell(9).setCellValue(invoice.getNonReimbursableExpenses());
        dataRow.createCell(10).setCellValue(invoice.getAssistantsSalaries());
        dataRow.createCell(11).setCellValue(invoice.getNetProfit());
        dataRow.createCell(12).setCellValue(customer.getName());
        dataRow.createCell(13).setCellValue(customer.getPhoneNumber());
        dataRow.createCell(14).setCellValue(customer.getEmail());
        dataRow.createCell(15).setCellValue(customer.getComment());
        dataRow.createCell(16).setCellValue(assistants.get(0).getFirstName());
        dataRow.createCell(17).setCellValue(assistants.get(0).getLastName());
        dataRow.createCell(18).setCellValue(assistants.get(0).getMiddleName());
        dataRow.createCell(19).setCellValue(assistants.get(0).getPhoneNumber());
        dataRow.createCell(20).setCellValue(assistants.get(0).getEmail());
        dataRow.createCell(21).setCellValue(assistants.get(0).getComment());

        Row dataRow1 = sheet.createRow(2);
        dataRow1.createCell(16).setCellValue(assistants.get(1).getFirstName());
        dataRow1.createCell(17).setCellValue(assistants.get(1).getLastName());
        dataRow1.createCell(18).setCellValue(assistants.get(1).getMiddleName());
        dataRow1.createCell(19).setCellValue(assistants.get(1).getPhoneNumber());
        dataRow1.createCell(20).setCellValue(assistants.get(1).getEmail());
        dataRow1.createCell(21).setCellValue(assistants.get(1).getComment());

        Row dataRow2 = sheet.createRow(3);
        dataRow2.createCell(16).setCellValue(assistants.get(2).getFirstName());
        dataRow2.createCell(17).setCellValue(assistants.get(2).getLastName());
        dataRow2.createCell(18).setCellValue(assistants.get(2).getMiddleName());
        dataRow2.createCell(19).setCellValue(assistants.get(2).getPhoneNumber());
        dataRow2.createCell(20).setCellValue(assistants.get(2).getEmail());
        dataRow2.createCell(21).setCellValue(assistants.get(2).getComment());

        Row dataRow3 = sheet.createRow(4);
        dataRow3.createCell(16).setCellValue(assistants.get(3).getFirstName());
        dataRow3.createCell(17).setCellValue(assistants.get(3).getLastName());
        dataRow3.createCell(18).setCellValue(assistants.get(3).getMiddleName());
        dataRow3.createCell(19).setCellValue(assistants.get(3).getPhoneNumber());
        dataRow3.createCell(20).setCellValue(assistants.get(3).getEmail());
        dataRow3.createCell(21).setCellValue(assistants.get(3).getComment());

        Row dataRow4 = sheet.createRow(5);
        dataRow4.createCell(16).setCellValue(assistants.get(4).getFirstName());
        dataRow4.createCell(17).setCellValue(assistants.get(4).getLastName());
        dataRow4.createCell(18).setCellValue(assistants.get(4).getMiddleName());
        dataRow4.createCell(19).setCellValue(assistants.get(4).getPhoneNumber());
        dataRow4.createCell(20).setCellValue(assistants.get(4).getEmail());
        dataRow4.createCell(21).setCellValue(assistants.get(4).getComment());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename= project_" + project.getName() + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
