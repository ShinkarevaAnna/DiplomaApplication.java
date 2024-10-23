package com.example.demo.service;

import com.example.demo.model.db.entity.*;
import com.example.demo.model.db.repository.ProjectRepository;
import com.example.demo.model.db.repository.UserRepository;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.dto.response.InvoiceInfoResponse;
import com.example.demo.model.dto.response.ReportFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {
    @InjectMocks
    private ExcelService excelService;

    @Mock
    private ProjectsService projectsService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private AssistantService assistantService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Mock
    private ProjectRepository projectRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    public void downloadProjectByIdAsExcel()  {

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
        List<AssistantInfoResponse> assistantInfoResponse = new ArrayList<>();
        for (Assistant as : assistants) {
            AssistantInfoResponse infoResponse = mapper.convertValue(as, AssistantInfoResponse.class);
            assistantInfoResponse.add(infoResponse);
        }
        project.setAssistants(new ArrayList<>());
        project.getAssistants().add(assistant);
        project.setCustomer(customer);
        project.setUser(user);
        project.setInvoice(invoice);
        invoice.setNetProfit(30000);
        customer.setPhoneNumber("+7924568954");

        when(projectsService.getProjectById(project.getId())).thenReturn(project);
        when(mapper.convertValue(invoiceService.getProjectInvoice(invoice.getId()), Invoice.class)).thenReturn(invoice);
        when(assistantService.getProjectAssistantsWithoutPagination(project.getId())).thenReturn(assistantInfoResponse);
        when(mapper.convertValue(customerService.getCustomer(customer.getId()), Customer.class)).thenReturn(customer);

        ReportFile reportFile = excelService.downloadProjectByIdAsExcel(project.getId());


        assertNotNull(reportFile);

    }
}