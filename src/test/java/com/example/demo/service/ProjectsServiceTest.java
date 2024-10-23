package com.example.demo.service;

import com.example.demo.model.db.entity.*;
import com.example.demo.model.db.repository.ProjectRepository;
import com.example.demo.model.dto.request.ProjectInfoRequest;
import com.example.demo.model.dto.request.ProjectToAssistantRequest;
import com.example.demo.model.dto.response.ProjectInfoResponse;
import com.example.demo.model.enums.ProjectsStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectsServiceTest {

    @InjectMocks
    private ProjectsService projectsService;
    @Mock
    private UserService userService;
    @Mock
    private CustomerService customerService;
    @Mock
    private GuestService guestService;
    @Mock
    private InvoiceService invoiceService;
    @Mock
    private AssistantService assistantService;
    @Mock
    private ProjectRepository projectRepository;
    @Spy
    private ObjectMapper mapper;

    @Test
    public void createProject() {
        ProjectInfoRequest request = new ProjectInfoRequest();
        request.setObject("Igora");
        request.setDateOfProjects(LocalDate.of(2022, 12, 12));

        Project project = new Project();
        project.setId(1L);

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectInfoResponse result = projectsService.createProject(request);

        assertEquals(project.getId(), result.getId());
    }

    @Test
    public void getProject() {
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        ProjectInfoResponse result = projectsService.getProject(1L);
        ProjectInfoResponse expected = mapper.convertValue(project, ProjectInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test
    public void updateProject() {
        Long carId = 1L;
        ProjectInfoRequest request = new ProjectInfoRequest();
        request.setObject("Igora");
        request.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project = new Project();
        project.setId(carId);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        projectsService.updateProject(project.getId(), request);
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(ProjectsStatus.UPDATED, project.getStatus());
    }

    @Test
    public void deleteProject() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));

        projectsService.deleteProject(project.getId());

        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(ProjectsStatus.DELETED, project.getStatus());
    }

    @Test
    public void getAllProjects() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project1 = new Project();
        project1.setId(2L);
        project1.setObject("Lachta");
        project1.setDateOfProjects(LocalDate.of(2018, 06, 23));
        List<Project> projects = List.of(project, project1);
        Page<Project> prijectPage = new PageImpl<>(projects, pageRequest, projects.size());
        when(projectRepository.findAllByStatusNot(pageRequest, ProjectsStatus.DELETED)).thenReturn(prijectPage);
        Page<ProjectInfoResponse> result = projectsService.getAllProjects(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(project.getId(), result.getContent().get(0).getId());
        assertEquals("Igora", result.getContent().get(0).getObject());
        assertEquals(LocalDate.of(2022, 12, 12), result.getContent().get(0).getDateOfProjects());
        assertEquals(project1.getId(), result.getContent().get(1).getId());
        assertEquals("Lachta", result.getContent().get(1).getObject());
        assertEquals(LocalDate.of(2018, 06, 23), result.getContent().get(1).getDateOfProjects());
    }

    @Test
    public void addProjectToUser() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        User user = new User();
        user.setId(1L);
        project.setUser(user);
        when(userService.getUserFromDB(user.getId())).thenReturn(user);
        projectsService.addProjectToUser(project.getId(), user.getId());
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(user.getId(), project.getUser().getId());

    }

    @Test
    public void addProjectToCustomer() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        Customer customer = new Customer();
        customer.setId(1L);
        project.setCustomer(customer);
        when(customerService.getCustomerFromDB(customer.getId())).thenReturn(customer);
        projectsService.addProjectToCustomer(project.getId(), customer.getId());
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(customer.getId(), project.getCustomer().getId());
    }

    @Test
    public void addProjectToGuest() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        Guest guest = new Guest();
        guest.setId(1L);
        project.setGuest(guest);
        when(guestService.getGuestFromDB(guest.getId())).thenReturn(guest);
        projectsService.addProjectToGuest(project.getId(), guest.getId());
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(guest.getId(), project.getGuest().getId());
    }

    @Test
    public void addProjectToInvoice() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        project.setInvoice(invoice);
        when(invoiceService.getInvoiceFromDB(invoice.getId())).thenReturn(invoice);
        projectsService.addProjectToInvoice(project.getId(), invoice.getId());
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(invoice.getId(), project.getInvoice().getId());
    }

    @Test
    public void addProjectToAssistant() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setProjects(new ArrayList<>());
        project.setAssistants(new ArrayList<>());
        when(assistantService.getAssistantFromDB(assistant.getId())).thenReturn(assistant);
        when(assistantService.updateAssistantData(any(Assistant.class))).thenReturn(assistant);
        ProjectToAssistantRequest request = ProjectToAssistantRequest.builder()
                .projectId(project.getId())
                .assistantId(assistant.getId())
                .build();
        projectsService.addProjectToAssistant(request);
        verify(projectRepository, times(1)).save(any(Project.class));
        assertEquals(assistant.getId(), project.getAssistants().get(0).getId());
        assertTrue(assistant.getProjects().contains(project));
    }

    @Test
    public void getUserProjects() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project1 = new Project();
        project1.setId(2L);
        project1.setObject("Lachta");
        project1.setDateOfProjects(LocalDate.of(2018, 06, 23));
        User user = new User();
        user.setId(1L);
        List<Project> projects = List.of(project, project1);
        Page<Project> projectPage = new PageImpl<>(projects, pageRequest, projects.size());
        when(projectRepository.findAllUserProjectsByStatusNot(user.getId(), pageRequest, ProjectsStatus.DELETED)).thenReturn(projectPage);
        project.setUser(user);
        project1.setUser(user);
        Page<ProjectInfoResponse> result = projectsService.getUserProjects(user.getId(), 0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(project.getId(), result.getContent().get(0).getId());
        assertEquals("Igora", result.getContent().get(0).getObject());
        assertEquals(LocalDate.of(2022, 12, 12), result.getContent().get(0).getDateOfProjects());
        assertEquals(project1.getId(), result.getContent().get(1).getId());
        assertEquals("Lachta", result.getContent().get(1).getObject());
        assertEquals(LocalDate.of(2018, 06, 23), result.getContent().get(1).getDateOfProjects());
    }

    @Test
    public void getGuestProjects() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project1 = new Project();
        project1.setId(2L);
        project1.setObject("Lachta");
        project1.setDateOfProjects(LocalDate.of(2018, 06, 23));
        Guest guest = new Guest();
        guest.setId(1L);
        List<Project> projects = List.of(project, project1);
        Page<Project> projectPage = new PageImpl<>(projects, pageRequest, projects.size());
        when(projectRepository.findAllGuestProjectsByStatusNot(guest.getId(), pageRequest, ProjectsStatus.DELETED)).thenReturn(projectPage);
        project.setGuest(guest);
        project1.setGuest(guest);
        Page<ProjectInfoResponse> result = projectsService.getGuestProjects(guest.getId(), 0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(project.getId(), result.getContent().get(0).getId());
        assertEquals("Igora", result.getContent().get(0).getObject());
        assertEquals(LocalDate.of(2022, 12, 12), result.getContent().get(0).getDateOfProjects());
        assertEquals(project1.getId(), result.getContent().get(1).getId());
        assertEquals("Lachta", result.getContent().get(1).getObject());
        assertEquals(LocalDate.of(2018, 06, 23), result.getContent().get(1).getDateOfProjects());
    }

    @Test
    public void getCustomerProjects() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project1 = new Project();
        project1.setId(2L);
        project1.setObject("Lachta");
        project1.setDateOfProjects(LocalDate.of(2018, 06, 23));
        Customer customer = new Customer();
        customer.setId(1L);
        List<Project> projects = List.of(project, project1);
        Page<Project> projectPage = new PageImpl<>(projects, pageRequest, projects.size());
        when(projectRepository.findAllCustomerProjectsByStatusNot(customer.getId(), pageRequest, ProjectsStatus.DELETED)).thenReturn(projectPage);
        project.setCustomer(customer);
        project1.setCustomer(customer);
        Page<ProjectInfoResponse> result = projectsService.getCustomerProjects(customer.getId(), 0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(project.getId(), result.getContent().get(0).getId());
        assertEquals("Igora", result.getContent().get(0).getObject());
        assertEquals(LocalDate.of(2022, 12, 12), result.getContent().get(0).getDateOfProjects());
        assertEquals(project1.getId(), result.getContent().get(1).getId());
        assertEquals("Lachta", result.getContent().get(1).getObject());
        assertEquals(LocalDate.of(2018, 06, 23), result.getContent().get(1).getDateOfProjects());
    }

    @Test
    public void getAssistantProjects() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Project project = new Project();
        project.setId(1L);
        project.setObject("Igora");
        project.setDateOfProjects(LocalDate.of(2022, 12, 12));
        Project project1 = new Project();
        project1.setId(2L);
        project1.setObject("Lachta");
        project1.setDateOfProjects(LocalDate.of(2018, 06, 23));
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        List<Project> projects = List.of(project, project1);
        Page<Project> projectPage = new PageImpl<>(projects, pageRequest, projects.size());
        when(projectRepository.findAllAssistantProjectsByStatusNot(assistant.getId(), pageRequest, ProjectsStatus.DELETED)).thenReturn(projectPage);
        assistant.setProjects(projects);
        Page<ProjectInfoResponse> result = projectsService.getAssistantProjects(assistant.getId(), 0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(project.getId(), result.getContent().get(0).getId());
        assertEquals("Igora", result.getContent().get(0).getObject());
        assertEquals(LocalDate.of(2022, 12, 12), result.getContent().get(0).getDateOfProjects());
        assertEquals(project1.getId(), result.getContent().get(1).getId());
        assertEquals("Lachta", result.getContent().get(1).getObject());
        assertEquals(LocalDate.of(2018, 06, 23), result.getContent().get(1).getDateOfProjects());
    }
}