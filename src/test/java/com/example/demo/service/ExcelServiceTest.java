package com.example.demo.service;

import com.example.demo.model.db.entity.*;
import com.example.demo.model.dto.response.ReportFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {
    @InjectMocks
    private ExcelService excelService;

    @Mock
    private ProjectsService projectsService;

    @Mock
    private AssistantService assistantService;

    @Mock
    private CustomerService customerService;

    @Spy
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    public void downloadProjectByIdAsExcel() {

        Project project = new Project();
        project.setId(1L);
        project.setName("fkjd");
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setFirstName("john");
        Customer customer = new Customer();
        customer.setId(1L);
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setProjectIncome(35067);
        invoice.setReimbursableExpenses(2000);
        invoice.setNonReimbursableExpenses(0);
        invoice.setAssistantsSalaries(200);
        User user = new User();
        user.setId(1L);
        List<Assistant> assistants = List.of(assistant);
        project.setAssistants(new ArrayList<>());
        project.getAssistants().add(assistant);
        project.setCustomer(customer);
        project.setUser(user);
        project.setInvoice(invoice);
        invoice.setNetProfit(30000);
        customer.setPhoneNumber("+7924568954");

        when(projectsService.getProjectById(project.getId())).thenReturn(project);
        when(mapper.convertValue(projectsService.getProjectInvoice(invoice.getId()), Invoice.class)).thenReturn(invoice);
        when(assistantService.getProjectAssistantsWithoutPagination(project.getId())).thenReturn(assistants);
        when(mapper.convertValue(customerService.getCustomer(customer.getId()), Customer.class)).thenReturn(customer);

        ReportFile reportFile = excelService.downloadProjectByIdAsExcel(project.getId());


        assertNotNull(reportFile);

    }
}