package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.*;
import com.example.demo.model.db.repository.ProjectRepository;
import com.example.demo.model.dto.request.*;
import com.example.demo.model.dto.response.InvoiceInfoResponse;
import com.example.demo.model.dto.response.ProjectInfoResponse;

import com.example.demo.model.enums.ProjectsStatus;
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
public class ProjectsService {
    private final UserService userService;
    private final AssistantService assistantService;
    private final ObjectMapper mapper;
    private final ProjectRepository projectRepository;
    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final GuestService guestService;

    @Transactional
    public ProjectInfoResponse createProject(ProjectInfoRequest request) {
        Project project = mapper.convertValue(request, Project.class);
        project.setStatus(ProjectsStatus.CREATED);
        return mapper.convertValue(projectRepository.save(project), ProjectInfoResponse.class);

    }

    public ProjectInfoResponse getProject(Long id) {
        return mapper.convertValue(getProjectById(id), ProjectInfoResponse.class);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ProjectInfoResponse updateProject(Long id, ProjectInfoRequest request) {
        Project project = getProjectById(id);

        project.setDateOfProjects((request.getDateOfProjects() == null) ? project.getDateOfProjects() : request.getDateOfProjects());
        project.setComment(request.getComment() == null ? project.getComment() : request.getComment());
        project.setObject(request.getObject() == null ? project.getObject() : request.getObject());
        project.setUpdateAt(LocalDateTime.now());
        project.setStatus(ProjectsStatus.UPDATED);

        Project save = projectRepository.save(project);

        return mapper.convertValue(save, ProjectInfoResponse.class);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        project.setStatus(ProjectsStatus.DELETED);
        projectRepository.save(project);
    }

    public Page<ProjectInfoResponse> getAllProjects(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Project> all;
        if (filter == null) {
            all = projectRepository.findAllByStatusNot(pageRequest, ProjectsStatus.DELETED);
        } else {
            all = projectRepository.findAllByStatusNotFiltered(pageRequest, ProjectsStatus.DELETED, filter.toUpperCase());
        }

        List<ProjectInfoResponse> content = all.getContent().stream()
                .map(project -> mapper.convertValue(project, ProjectInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
//    @Transactional
//    public void addProjectToUser(Long projectId, Long userId) {
//        Project project = getProjectById(projectId);
//        User userFromDB = userService.getUserFromDB(userId);
//        project.setUser(userFromDB);
//        projectRepository.save(project);
//
//    }
@Transactional
public void addProjectToUser(ProjectToUserRequest request) {
    Project project = getProjectById(request.getProjectId());
    User userFromDB = userService.getUserFromDB(request.getUserId());
    project.setUser(userFromDB);
    projectRepository.save(project);

}

//    @Transactional
//    public void addProjectToCustomer(Long projectId, Long customerId) {
//        Project project = getProjectById(projectId);
//        Customer customerFromDB = customerService.getCustomerFromDB(customerId);
//        project.setCustomer(customerFromDB);
//        projectRepository.save(project);
//    }
@Transactional
public void addProjectToCustomer(ProjectToCustomerInfoRequest request) {
    Project project = getProjectById(request.getProjectId());
    Customer customerFromDB = customerService.getCustomerFromDB(request.getCustomerId());
    project.setCustomer(customerFromDB);
    projectRepository.save(project);
}
//    @Transactional
//    public void addProjectToGuest(Long projectId, Long guestId) {
//        Project project = getProjectById(projectId);
//        Guest guestFromDB = guestService.getGuestFromDB(guestId);
//        project.setGuest(guestFromDB);
//        projectRepository.save(project);
//    }
@Transactional
public void addProjectToGuest(ProjectToGuestInfoRequest request) {
    Project project = getProjectById(request.getProjectId());
    Guest guestFromDB = guestService.getGuestFromDB(request.getGuestId());
    project.setGuest(guestFromDB);
    projectRepository.save(project);
}
//    @Transactional
//    public void addProjectToInvoice(Long projectId, Long invoiceId) {
//        Project project = getProjectById(projectId);
//        Invoice invoice = invoiceService.getInvoiceFromDB(invoiceId);
//        project.setInvoice(invoice);
//        projectRepository.save(project);

//    }
@Transactional
    public void addProjectToInvoice(ProjectToInvoiceInfoRequest request) {
    Project project = getProjectById(request.getProjectId());
    Invoice invoice = invoiceService.getInvoiceFromDB(request.getInvoiceId());
    project.setInvoice(invoice);
    projectRepository.save(project);
}
    @Transactional
    public void addProjectToAssistant(ProjectToAssistantRequest request) {
        Project project = getProjectById(request.getProjectId());
        Assistant assistantFromDB = assistantService.getAssistantFromDB(request.getAssistantId());
        assistantFromDB.getProjects().add(project);
        assistantService.updateAssistantData(assistantFromDB);
        project.getAssistants().add(assistantFromDB);
        projectRepository.save(project);

    }


    public Page<ProjectInfoResponse> getUserProjects(Long id, Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> userProjects;
        if (filter == null) {
            userProjects = projectRepository.findAllUserProjectsByStatusNot(id, pageRequest, ProjectsStatus.DELETED);
        } else {
            userProjects = projectRepository.findAllUserProjectsByStatusNotFiltered(id, pageRequest, ProjectsStatus.DELETED, filter.toLowerCase());
        }

        List<ProjectInfoResponse> content = userProjects.getContent().stream()
                .map(project -> mapper.convertValue(project, ProjectInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, userProjects.getTotalElements());
    }

    public Page<ProjectInfoResponse> getGuestProjects(Long id, Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> guestProjects;
        if (filter == null) {
            guestProjects = projectRepository.findAllGuestProjectsByStatusNot(id, pageRequest, ProjectsStatus.DELETED);
        } else {
            guestProjects = projectRepository.findAllGuestProjectsByStatusNotFiltered(id, pageRequest, ProjectsStatus.DELETED, filter.toLowerCase());
        }

        List<ProjectInfoResponse> content = guestProjects.getContent().stream()
                .map(project -> mapper.convertValue(project, ProjectInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, guestProjects.getTotalElements());
    }

    public Page<ProjectInfoResponse> getCustomerProjects(Long id, Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> customerProjects;
        if (filter == null) {
            customerProjects = projectRepository.findAllCustomerProjectsByStatusNot(id, pageRequest, ProjectsStatus.DELETED);
        } else {
            customerProjects = projectRepository.findAllCustomerProjectsByStatusNotFiltered(id, pageRequest, ProjectsStatus.DELETED, filter.toLowerCase());
        }

        List<ProjectInfoResponse> content = customerProjects.getContent().stream()
                .map(project -> mapper.convertValue(project, ProjectInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, customerProjects.getTotalElements());
    }

    public Page<ProjectInfoResponse> getAssistantProjects(Long id, Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Project> assistantProjects;
        if (filter == null) {
            assistantProjects = projectRepository.findAllAssistantProjectsByStatusNot(id, pageRequest, ProjectsStatus.DELETED);
        } else {
            assistantProjects = projectRepository.findAllAssistantProjectsByStatusNotFiltered(id, pageRequest, ProjectsStatus.DELETED, filter.toLowerCase());
        }

        List<ProjectInfoResponse> content = assistantProjects.getContent().stream()
                .map(project -> mapper.convertValue(project, ProjectInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, assistantProjects.getTotalElements());
    }
    public InvoiceInfoResponse getProjectInvoice(Long projectId) {
        Project project = getProjectById(projectId);
        return mapper.convertValue(invoiceService.getInvoiceFromDB(project.getInvoice().getId()), InvoiceInfoResponse.class);
    }
}
