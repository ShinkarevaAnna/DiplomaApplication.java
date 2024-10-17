package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Guest;
import com.example.demo.model.enums.GuestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    @Query("select c from Guest c where c.status <> :status")
    Page<Guest> findAllByStatusNot(Pageable request, GuestStatus status);

    @Query("select c from Guest c where c.status <> :status and (lower(c.createdAt) like %:filter%  or  lower(c.status) like %:filter% )")
    Page<Guest> findAllByStatusNotFiltered(Pageable request, GuestStatus status, @Param("filter") String filter);
}
