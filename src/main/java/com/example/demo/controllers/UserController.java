package com.example.demo.controllers;

import com.example.demo.model.dto.request.UserInfoRequest;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.constants.Constants.USERS;

@Tag(name = "Users")
@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user")
    public UserInfoResponse createUser(@RequestBody @Valid UserInfoRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserInfoResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id")
    public UserInfoResponse updateUser(@PathVariable Long id, @RequestBody UserInfoRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get user list")
    public Page<UserInfoResponse> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "lastName") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String filter

    ) {
        return userService.getAllUsers(page, perPage, sort, order, filter);
    }
}
