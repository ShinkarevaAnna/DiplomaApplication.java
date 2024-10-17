package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Invoice;
import com.example.demo.model.db.entity.Project;
import com.example.demo.model.db.repository.InvoiceRepository;
import com.example.demo.model.dto.request.InvoiceInfoRequest;
import com.example.demo.model.dto.response.InvoiceInfoResponse;
import com.example.demo.model.dto.response.ProjectInfoResponse;
import com.example.demo.model.enums.InvoiceStatus;
import com.example.demo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final ObjectMapper mapper;
    private final InvoiceRepository invoiceRepository;
    ProjectsService projectsService;

    @Transactional
    public InvoiceInfoResponse createInvoice(InvoiceInfoRequest request) {

        Invoice invoice = mapper.convertValue(request, Invoice.class);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.CREATED);
        Invoice save = invoiceRepository.save(invoice);
        return mapper.convertValue(save, InvoiceInfoResponse.class);
    }


    public InvoiceInfoResponse getInvoice(Long id) {
        Invoice invoice = getInvoiceFromDB(id);
        return mapper.convertValue(invoice, InvoiceInfoResponse.class);
    }

    public Invoice getInvoiceFromDB(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new CustomException("Invoice not found", HttpStatus.NOT_FOUND));
    }
    @Transactional
    public InvoiceInfoResponse updateInvoice(Long id, InvoiceInfoRequest request) {

        Invoice invoice = getInvoiceFromDB(id);

        invoice.setProjectIncome(request.getProjectIncome() == null ? invoice.getProjectIncome() : request.getProjectIncome());
        invoice.setReimbursableExpenses((request.getReimbursableExpenses() == null) ? invoice.getReimbursableExpenses() : request.getReimbursableExpenses());
        invoice.setNonReimbursableExpenses(request.getNonReimbursableExpenses() == null ? invoice.getNonReimbursableExpenses() : request.getNonReimbursableExpenses());
        invoice.setAssistantsSalaries(request.getAssistantsSalaries() == null ? invoice.getAssistantsSalaries() : request.getAssistantsSalaries());
        invoice.setNetProfit(request.getNetProfit() == null ? invoice.getNetProfit() : request.getNetProfit());

        invoice.setUpdateAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.UPDATED);

        Invoice save = invoiceRepository.save(invoice);

        return mapper.convertValue(save, InvoiceInfoResponse.class);
    }
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = getInvoiceFromDB(id);
        invoice.setUpdateAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.DELETED);
        invoiceRepository.save(invoice);

    }

    public Page<InvoiceInfoResponse> getAllInvoices(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {


        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Invoice> all;
        if (filter == null) {
            all = invoiceRepository.findAllByStatusNot(pageRequest, InvoiceStatus.DELETED);
        } else {
            all = invoiceRepository.findAllByStatusNotFiltered(pageRequest, InvoiceStatus.DELETED, filter.toLowerCase());
        }

        List<InvoiceInfoResponse> content = all.getContent().stream()
                .map(invoice -> mapper.convertValue(invoice, InvoiceInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }

    public InvoiceInfoResponse getProjectInvoice(Long projectId) {
        Project project = projectsService.getProjectById(projectId);
        return mapper.convertValue(getInvoiceFromDB(project.getInvoice().getId()), InvoiceInfoResponse.class);
    }
}
