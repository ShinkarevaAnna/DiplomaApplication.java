package com.example.demo.service;

import com.example.demo.model.db.entity.Customer;
import com.example.demo.model.db.repository.CustomerRepository;
import com.example.demo.model.dto.request.CustomerInfoRequest;
import com.example.demo.model.dto.response.CustomerInfoResponse;
import com.example.demo.model.enums.CustomerStatus;
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
public class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Spy
    private ObjectMapper mapper;

    @Test
    public void createCustomer() {
        CustomerInfoRequest request = new CustomerInfoRequest();
        request.setName("John");

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerInfoResponse result = customerService.createCustomer(request);

        assertEquals(customer.getId(), result.getId());
    }

    @Test
    public void getCustomer() {
        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("John");
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        CustomerInfoResponse result = customerService.getCustomer(1L);
        CustomerInfoResponse expected = mapper.convertValue(customer, CustomerInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test
    public void updateCustomer() {
        Long customerId = 1L;
        CustomerInfoRequest request = new CustomerInfoRequest();
        request.setName("John");
        request.setEmail("test@test.com");
        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        customerService.updateCustomer(customer.getId(), request);
        verify(customerRepository, times(1)).save(any(Customer.class));
        assertEquals(CustomerStatus.UPDATED, customer.getStatus());
    }

    @Test
    public void deleteCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(customer.getId());

        verify(customerRepository, times(1)).save(any(Customer.class));
        assertEquals(CustomerStatus.DELETED, customer.getStatus());
    }

    @Test
    public void getAllCustomers() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setEmail("john@goole.com");
        Customer customer1 = new Customer();
        customer1.setId(2L);
        customer1.setName("Tanya");
        customer1.setEmail("grotter@goole.com");
        List<Customer> customers = List.of(customer, customer1);
        Page<Customer> customersPage = new PageImpl<>(customers, pageRequest, customers.size());
        when(customerRepository.findAllByStatusNot(pageRequest, CustomerStatus.DELETED)).thenReturn(customersPage);
        Page<CustomerInfoResponse> result = customerService.getAllCustomers(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(customer.getId(), result.getContent().get(0).getId());
        assertEquals("John", result.getContent().get(0).getName());
        assertEquals("john@goole.com", result.getContent().get(0).getEmail());
        assertEquals(customer1.getId(), result.getContent().get(1).getId());
        assertEquals("Tanya", result.getContent().get(1).getName());
        assertEquals("grotter@goole.com", result.getContent().get(1).getEmail());
    }
}