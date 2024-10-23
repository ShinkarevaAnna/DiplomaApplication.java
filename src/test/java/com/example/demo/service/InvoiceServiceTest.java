package com.example.demo.service;

import com.example.demo.model.db.entity.Invoice;
import com.example.demo.model.db.entity.Project;
import com.example.demo.model.db.repository.InvoiceRepository;
import com.example.demo.model.dto.request.InvoiceInfoRequest;
import com.example.demo.model.dto.response.InvoiceInfoResponse;
import com.example.demo.model.enums.InvoiceStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {
    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createInvoice() {
        InvoiceInfoRequest request = new InvoiceInfoRequest();
        request.setProjectIncome(10000);
        request.setNonReimbursableExpenses(0);
        request.setAssistantsSalaries(0);
        Invoice invoice = new Invoice();
        invoice.setId(1L);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        InvoiceInfoResponse result = invoiceService.createInvoice(request);

        assertEquals(invoice.getId(), result.getId());
    }

    @Test
    public void getInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setProjectIncome(10000);
        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
        InvoiceInfoResponse result = invoiceService.getInvoice(1L);
        InvoiceInfoResponse expected = mapper.convertValue(invoice, InvoiceInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test
    public void updateInvoice() {
        Long invoiceId = 1L;
        InvoiceInfoRequest request = new InvoiceInfoRequest();
        request.setProjectIncome(10000);
        request.setAssistantsSalaries(2000);
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
        invoiceService.updateInvoice(invoice.getId(), request);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        assertEquals(InvoiceStatus.UPDATED, invoice.getStatus());
    }

    @Test
    public void deleteInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);

        when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(invoice.getId());

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        assertEquals(InvoiceStatus.DELETED, invoice.getStatus());

    }

    @Test
    public void getAllInvoices() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setProjectIncome(10000);
        invoice.setAssistantsSalaries(2000);
        Invoice invoice1 = new Invoice();
        invoice1.setId(2L);
        invoice1.setProjectIncome(20000);
        invoice1.setAssistantsSalaries(2000);
        List<Invoice> invoices = List.of(invoice, invoice1);
        Page<Invoice> invoicePage = new PageImpl<>(invoices, pageRequest, invoices.size());
        when(invoiceRepository.findAllByStatusNot(pageRequest, InvoiceStatus.DELETED)).thenReturn(invoicePage);
        Page<InvoiceInfoResponse> result = invoiceService.getAllInvoices(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(invoice.getId(), result.getContent().get(0).getId());
        assertEquals((Integer) 10000, result.getContent().get(0).getProjectIncome());
        assertEquals((Integer) 2000, result.getContent().get(0).getAssistantsSalaries());
        assertEquals(invoice1.getId(), result.getContent().get(1).getId());
        assertEquals((Integer) 20000, result.getContent().get(1).getProjectIncome());
        assertEquals((Integer) 2000, result.getContent().get(1).getAssistantsSalaries());
    }

    @Test
    public void getProjectInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setProjectIncome(10000);
        invoice.setAssistantsSalaries(2000);
        Project project = new Project();
        project.setId(1L);
        project.setInvoice(invoice);
        assertEquals(invoice.getId(), project.getInvoice().getId());
        assertEquals((Integer) 10000, project.getInvoice().getProjectIncome());
        assertEquals((Integer) 2000, project.getInvoice().getAssistantsSalaries());
    }
}