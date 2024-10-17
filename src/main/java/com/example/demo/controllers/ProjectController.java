package com.example.demo.controllers;

import com.example.demo.model.dto.request.ProjectInfoRequest;
import com.example.demo.model.dto.request.ProjectToAssistantRequest;
import com.example.demo.model.dto.response.ProjectInfoResponse;
import com.example.demo.service.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.constants.Constants.PROJECTS;

@Tag(name = "Projects")
@RestController
@RequestMapping(PROJECTS)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectsService projectsService;

    @PostMapping
    @Operation(summary = "Create project")
    public ProjectInfoResponse createProject(@RequestBody @Valid ProjectInfoRequest request) {
        return projectsService.createProject(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by id")
    public ProjectInfoResponse getProject(@PathVariable Long id) {
        return projectsService.getProject(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project by id")
    public ProjectInfoResponse updateProject(@PathVariable Long id, @RequestBody ProjectInfoRequest request) {
        return projectsService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project by id")
    public void deleteProject(@PathVariable Long id) {
        projectsService.deleteProject(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get project list")
    public Page<ProjectInfoResponse> getAllProjects(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "lastName") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter

    ) {
        return projectsService.getAllProjects(page, perPage, sort, order, filter);
    }

    @PostMapping("/projectToUser/{id}")
    @Operation(summary = "Add project to user")
    public void addProjectToUser(@PathVariable Long projectId, @PathVariable Long userId) {
        projectsService.addProjectToUser(projectId, userId);
    }

    @PostMapping("/projectToAssistant/{id}")
    @Operation(summary = "Add project to assistant")
    public void addProjectToAssistant(@RequestBody ProjectToAssistantRequest request){
        projectsService.addProjectToAssistant(request);
    }
    @PostMapping("/projectToInvoice/{id}")
    @Operation(summary = "Add project to invoice")
    public void addProjectToInvoice(@PathVariable Long projectId, @PathVariable Long invoiceId) {
        projectsService.addProjectToInvoice(projectId, invoiceId);
    }
    @PostMapping("/projectToGuest/{id}")
    @Operation(summary = "Add project to guest")
    public void addProjectToGuest(@PathVariable Long projectId, @PathVariable Long guestId){
        projectsService.addProjectToGuest(projectId, guestId);
    }

    @PostMapping("/projectToCustomer/{id}")
    @Operation(summary = "Add project to customer")
    public  void addProjectToCustomer(@PathVariable Long projectId, @PathVariable Long customerId){
        projectsService.addProjectToCustomer(projectId, customerId);
    }
    @GetMapping("/guestProjects/{id}")
    @Operation(summary = "get guest projects")
    public Page<ProjectInfoResponse> getGuestProjects(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "20") Integer perPage,
                                                     @RequestParam(defaultValue = "dateOfProjects") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam(required = false) String filter

    ) {
        return projectsService.getGuestProjects(id, page, perPage, sort, order, filter);
    }


    @GetMapping("/customerProjects/{id}")
    @Operation(summary = "get customer projects")
    public Page<ProjectInfoResponse> getCustomerProjects(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "20") Integer perPage,
                                                     @RequestParam(defaultValue = "dateOfProjects") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam(required = false) String filter

    ) {
        return projectsService.getCustomerProjects(id, page, perPage, sort, order, filter);
    }

    @GetMapping("/userProjects/{id}")
    @Operation(summary = "get user projects")
    public Page<ProjectInfoResponse> getUserProjects(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "20") Integer perPage,
                                                   @RequestParam(defaultValue = "dateOfProjects") String sort,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                   @RequestParam(required = false) String filter

    ) {
        return projectsService.getUserProjects(id, page, perPage, sort, order, filter);
    }

    @GetMapping("/assistantProjects/{id}")
    @Operation(summary = "get assistant projects")
    public Page<ProjectInfoResponse> getAssistantProjects(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "15") Integer perPage,
                                                     @RequestParam(defaultValue = "dateOfProjects") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam(required = false) String filter

    ) {
        return projectsService.getAssistantProjects(id, page, perPage, sort, order, filter);
    }
}
