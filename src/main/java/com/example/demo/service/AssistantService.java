package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Assistant;
import com.example.demo.model.db.entity.Project;
import com.example.demo.model.db.repository.AssistantRepository;
import com.example.demo.model.dto.request.AssistantInfoRequest;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.enums.AssistantStatus;
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
public class AssistantService {
    private final ObjectMapper mapper;
    private final AssistantRepository assistantRepository;

    @Transactional
    public AssistantInfoResponse createAssistant(AssistantInfoRequest request) {

        Assistant assistant = mapper.convertValue(request, Assistant.class);
        assistant.setCreatedAt(LocalDateTime.now());
        assistant.setStatus(AssistantStatus.CREATED);
        Assistant save = assistantRepository.save(assistant);
        return mapper.convertValue(save, AssistantInfoResponse.class);
    }


    public AssistantInfoResponse getAssistant(Long id) {
        Assistant assistant = getAssistantFromDB(id);
        return mapper.convertValue(assistant, AssistantInfoResponse.class);
    }

    Assistant getAssistantFromDB(Long id) {
        return assistantRepository.findById(id).orElseThrow(() -> new CustomException("Assistant not found", HttpStatus.NOT_FOUND));
    }
    @Transactional
    public AssistantInfoResponse updateAssistant(Long id, AssistantInfoRequest request) {

        Assistant assistant = getAssistantFromDB(id);

        assistant.setEmail(request.getEmail() == null ? assistant.getEmail() : request.getEmail());
        assistant.setComment(request.getComment() == null ? assistant.getComment() : request.getComment());
        assistant.setPhoneNumber((request.getPhoneNumber() == null) ? assistant.getPhoneNumber() : request.getPhoneNumber());
        assistant.setFirstName(request.getFirstName() == null ? assistant.getFirstName() : request.getFirstName());
        assistant.setLastName(request.getLastName() == null ? assistant.getLastName() : request.getLastName());
        assistant.setMiddleName(request.getMiddleName() == null ? assistant.getLastName() : request.getLastName());

        assistant.setUpdatedAt(LocalDateTime.now());
        assistant.setStatus(AssistantStatus.UPDATED);

        Assistant save = assistantRepository.save(assistant);

        return mapper.convertValue(save, AssistantInfoResponse.class);
    }
    @Transactional
    public void deleteAssistant(Long id) {
        Assistant assistant = getAssistantFromDB(id);
        assistant.setUpdatedAt(LocalDateTime.now());
        assistant.setStatus(AssistantStatus.DELETED);
        assistantRepository.save(assistant);

    }

    public Page<AssistantInfoResponse> getAllAssistants(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Assistant> all;
        if (filter == null) {
            all = assistantRepository.findAllByStatusNot(pageRequest, AssistantStatus.DELETED);
        } else {
            all = assistantRepository.findAllByStatusNotFiltered(pageRequest, AssistantStatus.DELETED, filter.toLowerCase());
        }

        List<AssistantInfoResponse> content = all.getContent().stream()
                .map(assistant -> mapper.convertValue(assistant, AssistantInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }

    public Assistant updateAssistantData(Assistant assistant) {
        return assistantRepository.save(assistant);
    }

    public Page<AssistantInfoResponse> getProjectAssistants(Long id, Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Assistant> projectAssistant;
        if (filter == null) {
            projectAssistant = assistantRepository.findAllProjectAssistantsByStatusNot(id, pageRequest);
        } else {
            projectAssistant = assistantRepository.findAllProjectAssistantsByStatusNotFiltered(id, pageRequest, filter.toLowerCase());
        }

        List<AssistantInfoResponse> content = projectAssistant.getContent().stream()
                .map(assistant -> mapper.convertValue(assistant, AssistantInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, projectAssistant.getTotalElements());
    }
    public List<Assistant> getProjectAssistantsWithoutPagination(Long id){
        return assistantRepository.findAllProjectAssistantsWithoutPagination(id);

    }
}
