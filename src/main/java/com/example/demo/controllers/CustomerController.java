package com.example.demo.controllers;

import com.example.demo.model.dto.request.CustomerInfoRequest;
import com.example.demo.model.dto.response.CustomerInfoResponse;
import com.example.demo.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.CUSTOMERS;

@Tag(name = "Customers")
@RestController
@RequestMapping(CUSTOMERS)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create customer")
    public CustomerInfoResponse createCustomer(@RequestBody CustomerInfoRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    public CustomerInfoResponse getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer by id")
    public CustomerInfoResponse updateCustomer(@PathVariable Long id, @RequestBody CustomerInfoRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by id")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get customer list")
    public Page<CustomerInfoResponse> getAllCustomers(@RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer perPage,
                                                      @RequestParam(defaultValue = "lastName") String sort,
                                                      @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                      @RequestParam(required = false) String filter

    ) {
        return customerService.getAllCustomers(page, perPage, sort, order, filter);
    }
}
