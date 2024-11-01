package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Assistant;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.enums.AssistantStatus;
import com.example.demo.model.enums.ProjectsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssistantRepository extends JpaRepository<Assistant, Long> {


    @Query("select c from Assistant c where c.status <> :status")
    Page<Assistant> findAllByStatusNot(Pageable request, AssistantStatus status);

    @Query("select c from Assistant c where c.status <> :status and (lower(c.firstName) like %:filter%  or  lower(c.lastName) like %:filter% )")
    Page<Assistant> findAllByStatusNotFiltered(Pageable request, AssistantStatus status, @Param("filter") String filter);

    @Query(nativeQuery = true, value = "SELECT * FROM assistants a JOIN assistant_projects ap ON a.id = ap.assistant_id " +
            "JOIN projects p ON ap.project_id = p.id " +
            "WHERE p.id = :projectId AND p.status <> :status ")
    Page<Assistant> findAllProjectAssistantsByStatusNot(@Param("projectId") Long id, Pageable request, @Param("status") ProjectsStatus status);

    @Query(nativeQuery = true, value = "SELECT * FROM assistants a JOIN assistant_projects ap ON a.id = ap.assistant_id " +
            "JOIN projects p ON ap.project_id = p.id " +
            "WHERE p.id = :projectId AND p.status <> :status AND " +
            "(UPPER(cast(a.name as VARCHAR)) LIKE CONCAT('%', UPPER(:filter), '%') OR " +
            "UPPER(p.date_of_projects) LIKE CONCAT('%', UPPER(:filter), '%') OR " +
            "UPPER(p.object) LIKE CONCAT('%', UPPER(:filter), '%'))")
    Page<Assistant> findAllProjectAssistantsByStatusNotFiltered(@Param("projectId") Long id, Pageable request, @Param("status") ProjectsStatus status, @Param("filter") String filter);

    @Query(nativeQuery = true, value = "SELECT * FROM assistants a JOIN assistant_projects ap ON a.id = ap.assistant_id " +
            "WHERE ap.project_id = :projectId")
    List<Assistant> findAllProjectAssistantsWithoutPagination(@Param("projectId") Long id);
}
