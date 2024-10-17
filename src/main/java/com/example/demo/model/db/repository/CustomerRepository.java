package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Customer;
import com.example.demo.model.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.status <> :status")
    Page<Customer> findAllByStatusNot(Pageable request, CustomerStatus status);

    @Query("select c from Customer c where c.status <> :status and (lower(c.name) like %:filter%  or  lower(c.email) like %:filter% )")
    Page<Customer> findAllByStatusNotFiltered(Pageable request, CustomerStatus status, @Param("filter") String filter);
}
