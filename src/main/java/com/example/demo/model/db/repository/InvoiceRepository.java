package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Invoice;
import com.example.demo.model.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("select c from Invoice c where c.status <> :status")
    Page<Invoice> findAllByStatusNot(Pageable request, InvoiceStatus status);

    @Query("select c from Invoice c where c.status <> :status and (lower(c.projectIncome) like %:filter%  or  lower(c.netProfit) like %:filter% )")
    Page<Invoice> findAllByStatusNotFiltered(Pageable request, InvoiceStatus status, @Param("filter") String filter);
}
