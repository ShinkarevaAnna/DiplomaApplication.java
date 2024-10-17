package com.example.demo.controllers;

import com.example.demo.model.dto.request.GuestInfoRequest;
import com.example.demo.model.dto.response.GuestInfoResponse;
import com.example.demo.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.GUEST;

@Tag(name = "Guest")
@RestController
@RequestMapping(GUEST)
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;

    @PostMapping
    @Operation(summary = "Create guest")
    public GuestInfoResponse createGuest(@RequestBody GuestInfoRequest request) {
        return guestService.createGuest(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guest by id")
    public GuestInfoResponse getGuest(@PathVariable Long id) {
        return guestService.getGuest(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete guest by id")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Get guest list")
    public Page<GuestInfoResponse> getAllGuest(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer perPage,
                                               @RequestParam(defaultValue = "createdAt") String sort,
                                               @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                               @RequestParam(required = false) String filter

    ) {
        return guestService.getAllGuests(page, perPage, sort, order, filter);
    }

}
