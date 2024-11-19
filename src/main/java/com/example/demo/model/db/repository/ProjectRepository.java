package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Project;
import com.example.demo.model.enums.ProjectsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select c from Project c where c.status <> :status")
    Page<Project> findAllByStatusNot(Pageable request, ProjectsStatus status);

    @Query("select c from Project c where c.status <> :status and (upper(c.customer.name) like %:filter% or" +
            " upper(c.dateOfProjects) like %:filter%  or  upper(c.object) like %:filter% )")
    Page<Project> findAllByStatusNotFiltered(Pageable request, ProjectsStatus status, @Param("filter") String filter);

    @Query("select c from Project c where c.user.id = :id and c.status <> :status")
    Page<Project> findAllUserProjectsByStatusNot(Long id, Pageable request, ProjectsStatus status);

    @Query("select c from Project c where c.user.id = :id and c.status <> :status and (upper(c.customer.name ) like %:filter% or " +
            "upper(c.dateOfProjects) like %:filter%  or  upper(c.object) like %:filter% )")
    Page<Project> findAllUserProjectsByStatusNotFiltered(Long id, Pageable request, ProjectsStatus status, @Param("filter") String filter);

    @Query("select c from Project c where c.guest.id = :id and c.status <> :status")
    Page<Project> findAllGuestProjectsByStatusNot(Long id, Pageable request, ProjectsStatus status);

    @Query("select c from Project c where c.guest.id = :id and c.status <> :status and (upper(c.customer.name) like %:filter% or " +
            "upper(c.dateOfProjects) like %:filter%  or  upper(c.object) like %:filter% )")
    Page<Project> findAllGuestProjectsByStatusNotFiltered(Long id, Pageable request, ProjectsStatus status, @Param("filter") String filter);

    @Query("select c from Project c where c.customer.id = :id and c.status <> :status")
    Page<Project> findAllCustomerProjectsByStatusNot(Long id, Pageable request, ProjectsStatus status);

    @Query("select c from Project c where c.customer.id = :id and c.status <> :status and (upper(c.customer.name) like %:filter% or " +
            "upper(c.dateOfProjects) like %:filter%  or  upper(c.object) like %:filter% )")
    Page<Project> findAllCustomerProjectsByStatusNotFiltered(Long id, Pageable request, ProjectsStatus status, @Param("filter") String filter);

    @Query(nativeQuery = true, value = "SELECT * FROM project p JOIN assistant_projects ap ON p.id = ap.project_id " +
            "JOIN assistant a ON ap.assistant_id = a.id WHERE a.id = :id AND p.status <> :status")
    Page<Project> findAllAssistantProjectsByStatusNot(@Param("id") Long id, Pageable request, @Param("status") ProjectsStatus status);

    @Query(nativeQuery = true, value = "SELECT * FROM project p " +
            "JOIN assistant_projects ap ON p.id = ap.project_id " +
            "JOIN assistant a ON ap.assistant_id = a.id " +
            "WHERE a.id = :assistantId AND p.status <> :status AND " +
            "(UPPER(cast(a.name as VARCHAR)) LIKE CONCAT('%', UPPER(:filter), '%') OR " +
            "UPPER(p.date_of_projects) LIKE CONCAT('%', UPPER(:filter), '%') OR " +
            "UPPER(p.object) LIKE CONCAT('%', UPPER(:filter), '%'))")
    Page<Project> findAllAssistantProjectsByStatusNotFiltered(@Param("assistantId") Long id, Pageable request, @Param("status") ProjectsStatus status, @Param("filter") String filter);


}
