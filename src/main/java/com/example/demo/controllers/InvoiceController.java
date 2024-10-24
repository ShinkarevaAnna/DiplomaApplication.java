package com.example.demo.controllers;

import com.example.demo.model.dto.request.InvoiceInfoRequest;
import com.example.demo.model.dto.response.InvoiceInfoResponse;
import com.example.demo.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.INVOICE;

@Tag(name = "Invoice")
@RestController
@RequestMapping(INVOICE)
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create invoice")
    public InvoiceInfoResponse createInvoice(@RequestBody InvoiceInfoRequest request) {
        return invoiceService.createInvoice(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by id")
    public InvoiceInfoResponse getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoice(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update invoice by id")
    public InvoiceInfoResponse updateInvoice(@PathVariable Long id, @RequestBody InvoiceInfoRequest request) {
        return invoiceService.updateInvoice(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice by id")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get invoice list")
    public Page<InvoiceInfoResponse> getAllInvoices(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer perPage,
                                                     @RequestParam(defaultValue = "projectIncome") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam(required = false) String filter

    ) {
        return invoiceService.getAllInvoices(page, perPage, sort, order, filter);
    }

    @GetMapping("/projectInvoice/{id}")
    @Operation(summary = "get project invoice")
    public InvoiceInfoResponse getProjectInvoice(@PathVariable Long id) {
        return invoiceService.getProjectInvoice(id);
    }
}
