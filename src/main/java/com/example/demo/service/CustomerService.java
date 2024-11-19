package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Customer;
import com.example.demo.model.db.repository.CustomerRepository;
import com.example.demo.model.dto.request.CustomerInfoRequest;
import com.example.demo.model.dto.response.CustomerInfoResponse;
import com.example.demo.model.enums.CustomerStatus;
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
public class CustomerService {
    private final ObjectMapper mapper;
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerInfoResponse createCustomer(CustomerInfoRequest request) {
        Customer customer = mapper.convertValue(request, Customer.class);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.CREATED);
        Customer save = customerRepository.save(customer);
        return mapper.convertValue(save, CustomerInfoResponse.class);
    }

    public CustomerInfoResponse getCustomer(Long id) {
        Customer customer = getCustomerFromDB(id);
        return mapper.convertValue(customer, CustomerInfoResponse.class);
    }

    public Customer getCustomerFromDB(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public CustomerInfoResponse updateCustomer(Long id, CustomerInfoRequest request) {

        Customer customer = getCustomerFromDB(id);

        customer.setComment(request.getComment() == null ? customer.getComment() : request.getComment());
        customer.setName((request.getName() == null) ? customer.getName() : request.getName());
        customer.setEmail(request.getEmail() == null ? customer.getEmail() : request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber() == null ? customer.getPhoneNumber() : request.getPhoneNumber());

        customer.setUpdatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.UPDATED);

        Customer save = customerRepository.save(customer);

        return mapper.convertValue(save, CustomerInfoResponse.class);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerFromDB(id);
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.DELETED);
        customerRepository.save(customer);

    }

    public Page<CustomerInfoResponse> getAllCustomers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {


        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Customer> all;
        if (filter == null) {
            all = customerRepository.findAllByStatusNot(pageRequest, CustomerStatus.DELETED);
        } else {
            all = customerRepository.findAllByStatusNotFiltered(pageRequest, CustomerStatus.DELETED, filter.toLowerCase());
        }

        List<CustomerInfoResponse> content = all.getContent().stream()
                .map(customer -> mapper.convertValue(customer, CustomerInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
