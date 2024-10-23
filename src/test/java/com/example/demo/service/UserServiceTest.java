package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.User;
import com.example.demo.model.db.repository.UserRepository;
import com.example.demo.model.dto.request.UserInfoRequest;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserInfoResponse result = userService.createUser(request);

        assertEquals(user.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@..test.com");

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_userExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserInfoResponse result = userService.getUser(1L);
        UserInfoResponse expected = mapper.convertValue(user, UserInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test(expected = CustomException.class)
    public void getUser_noUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        UserInfoResponse result = userService.getUser(2L);
        UserInfoResponse expected = mapper.convertValue(user, UserInfoResponse.class);
        assertEquals(expected, result);

    }

    @Test
    public void updateUser() {
        Long userId = 1L;
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");
        request.setFirstName("John");
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.updateUser(user.getId(), request);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.UPDATED, user.getStatus());
    }

    @Test(expected = CustomException.class)
    public void updateUser_userAlreadyExists() {
        Long userId = 1L;
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");
        User user = new User();
        user.setId(userId);
        user.setEmail("test@test.com");
        when(userRepository.findByEmailIgnoreCase(request.getEmail())).thenReturn(Optional.of(user));
        userService.updateUser(user.getId(), request);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void getAllUsers() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("john.doe@example.com");
        user1.setFirstName("John");
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("grotter@example.com");
        user2.setFirstName("Tanya");
        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users, pageRequest, users.size());
        when(userRepository.findAllByStatusNot(pageRequest, UserStatus.DELETED)).thenReturn(userPage);
        Page<UserInfoResponse> result = userService.getAllUsers(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(user1.getId(), result.getContent().get(0).getId());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("john.doe@example.com", result.getContent().get(0).getEmail());
        assertEquals(user2.getId(), result.getContent().get(1).getId());
        assertEquals("Tanya", result.getContent().get(1).getFirstName());
        assertEquals("grotter@example.com", result.getContent().get(1).getEmail());
    }
}