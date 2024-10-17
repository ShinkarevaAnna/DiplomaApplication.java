package com.example.demo.controllers;

import com.example.demo.model.dto.request.AssistantInfoRequest;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.service.AssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.ASSISTANTS;

@Tag(name = "Assistant")
@RestController
@RequestMapping(ASSISTANTS)
@RequiredArgsConstructor
public class AssistantController {
    private final AssistantService assistantService;

    @PostMapping
    @Operation(summary = "Create assistant")
    public AssistantInfoResponse createAssistant(@RequestBody AssistantInfoRequest request) {
        return assistantService.createAssistant(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assistant by id")
    public AssistantInfoResponse getAssistant(@PathVariable Long id) {
        return assistantService.getAssistant(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update assistant by id")
    public AssistantInfoResponse updateAssistant(@PathVariable Long id, @RequestBody AssistantInfoRequest request) {
        return assistantService.updateAssistant(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete assistant by id")
    public void deleteAssistant(@PathVariable Long id) {
        assistantService.deleteAssistant(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get assistant list")
    public Page<AssistantInfoResponse> getAllAssistants(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer perPage,
                                                        @RequestParam(defaultValue = "lastName") String sort,
                                                        @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                        @RequestParam(required = false) String filter

    ) {
        return assistantService.getAllAssistants(page, perPage, sort, order, filter);
    }

    @GetMapping("/all/{id}")
    @Operation(summary = "Get project assistant list")
    public Page <AssistantInfoResponse> getProjectAssistants(@PathVariable Long id,
                                                             @RequestParam(defaultValue = "1") Integer page,
                                                             @RequestParam(defaultValue = "10") Integer perPage,
                                                             @RequestParam(defaultValue = "lastName") String sort,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                             @RequestParam(required = false) String filter)
    {
        return assistantService.getProjectAssistants(id, page, perPage, sort, order, filter);
    }
}
