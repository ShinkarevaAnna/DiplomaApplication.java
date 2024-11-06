package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Assistant;
import com.example.demo.model.db.entity.Project;
import com.example.demo.model.db.repository.AssistantRepository;
import com.example.demo.model.dto.request.AssistantInfoRequest;
import com.example.demo.model.dto.response.AssistantInfoResponse;
import com.example.demo.model.enums.AssistantStatus;
import com.example.demo.model.enums.ProjectsStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssistantServiceTest {
    @InjectMocks
    private AssistantService assistantService;
    @Mock
    private ProjectsService projectsService;

    @Mock
    private AssistantRepository assistantRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createAssistant() {
        AssistantInfoRequest request = new AssistantInfoRequest();
        request.setFirstName("John");

        Assistant assistant = new Assistant();
        assistant.setId(1L);

        when(assistantRepository.save(any(Assistant.class))).thenReturn(assistant);

        AssistantInfoResponse result = assistantService.createAssistant(request);

        assertEquals(assistant.getId(), result.getId());
    }

    @Test
    public void getAssistant() {
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setFirstName("John");
        when(assistantRepository.findById(assistant.getId())).thenReturn(Optional.of(assistant));
        AssistantInfoResponse result = assistantService.getAssistant(1L);
        AssistantInfoResponse expected = mapper.convertValue(assistant, AssistantInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test(expected = CustomException.class)
    public void getAssistant_noAssistant() {
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setFirstName("John");
        AssistantInfoResponse result = assistantService.getAssistant(2L);
        AssistantInfoResponse expected = mapper.convertValue(assistant, AssistantInfoResponse.class);
        assertEquals(expected, result);
    }


    @Test
    public void updateAssistant() {
        Long userId = 1L;
        AssistantInfoRequest request = new AssistantInfoRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        Assistant assistant = new Assistant();
        assistant.setId(userId);
        when(assistantRepository.findById(assistant.getId())).thenReturn(Optional.of(assistant));
        assistantService.updateAssistant(assistant.getId(), request);
        verify(assistantRepository, times(1)).save(any(Assistant.class));
        assertEquals(AssistantStatus.UPDATED, assistant.getStatus());
    }

    @Test
    public void deleteAssistant() {
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setFirstName("John");

        when(assistantRepository.findById(assistant.getId())).thenReturn(Optional.of(assistant));

        assistantService.deleteAssistant(assistant.getId());

        verify(assistantRepository, times(1)).save(any(Assistant.class));
        assertEquals(AssistantStatus.DELETED, assistant.getStatus());
    }

    @Test
    public void getAllAssistants() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setEmail("john.doe@example.com");
        assistant.setFirstName("John");
        Assistant assistant1 = new Assistant();
        assistant1.setId(2L);
        assistant1.setEmail("grotter@example.com");
        assistant1.setFirstName("Tanya");
        List<Assistant> assistants = List.of(assistant, assistant1);
        Page<Assistant> assistantPage = new PageImpl<>(assistants, pageRequest, assistants.size());
        when(assistantRepository.findAllByStatusNot(pageRequest, AssistantStatus.DELETED)).thenReturn(assistantPage);
        Page<AssistantInfoResponse> result = assistantService.getAllAssistants(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(assistant.getId(), result.getContent().get(0).getId());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("john.doe@example.com", result.getContent().get(0).getEmail());
        assertEquals(assistant1.getId(), result.getContent().get(1).getId());
        assertEquals("Tanya", result.getContent().get(1).getFirstName());
        assertEquals("grotter@example.com", result.getContent().get(1).getEmail());
    }

    @Test
    public void getProjectAssistants() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setEmail("john.doe@example.com");
        assistant.setFirstName("John");
        Assistant assistant1 = new Assistant();
        assistant1.setId(2L);
        assistant1.setEmail("grotter@example.com");
        assistant1.setFirstName("Tanya");
        Project project = new Project();
        project.setId(1L);
        List<Assistant> assistants = List.of(assistant, assistant1);
        Page<Assistant> assistantsPage = new PageImpl<>(assistants, pageRequest, assistants.size());
        when(assistantRepository.findAllProjectAssistantsByStatusNot(project.getId(), pageRequest)).thenReturn(assistantsPage);
        project.setAssistants(assistants);
        Page<AssistantInfoResponse> result = assistantService.getProjectAssistants(project.getId(), 0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(assistant.getId(), result.getContent().get(0).getId());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("john.doe@example.com", result.getContent().get(0).getEmail());
        assertEquals(assistant1.getId(), result.getContent().get(1).getId());
        assertEquals("Tanya", result.getContent().get(1).getFirstName());
        assertEquals("grotter@example.com", result.getContent().get(1).getEmail());
    }

    @Test
    public void getProjectAssistantsWithoutPagination() {
        Assistant assistant = new Assistant();
        assistant.setId(1L);
        assistant.setEmail("john.doe@example.com");
        assistant.setFirstName("John");
        Assistant assistant1 = new Assistant();
        assistant1.setId(2L);
        assistant1.setEmail("grotter@example.com");
        assistant1.setFirstName("Tanya");
        Project project = new Project();
        project.setId(1L);
        List<Assistant> assistants = List.of(assistant, assistant1);
//        List<AssistantInfoResponse> assistantInfoResponse = new ArrayList<>();
//        for (Assistant as : assistants) {
//            AssistantInfoResponse infoResponse = mapper.convertValue(as, AssistantInfoResponse.class);
//            assistantInfoResponse.add(infoResponse);
//        }
        when(assistantRepository.findAllProjectAssistantsWithoutPagination(project.getId())).thenReturn(assistants);
        project.setAssistants(assistants);
        List<Assistant> result = assistantService.getProjectAssistantsWithoutPagination(project.getId());
        assertEquals(2, result.size());
        assertEquals(assistant.getId(), result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        assertEquals(assistant1.getId(), result.get(1).getId());
        assertEquals("Tanya", result.get(1).getFirstName());
        assertEquals("grotter@example.com", result.get(1).getEmail());
    }
}
