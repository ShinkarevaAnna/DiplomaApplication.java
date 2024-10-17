package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Guest;
import com.example.demo.model.db.repository.GuestRepository;
import com.example.demo.model.dto.request.GuestInfoRequest;
import com.example.demo.model.dto.response.GuestInfoResponse;
import com.example.demo.model.enums.GuestStatus;
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
public class GuestService {
    private final ObjectMapper mapper;
    private final GuestRepository guestRepository;
    @Transactional
    public GuestInfoResponse createGuest(GuestInfoRequest request) {

        Guest guest = mapper.convertValue(request, Guest.class);
        guest.setCreatedAt(LocalDateTime.now());
        guest.setStatus(GuestStatus.CREATED);
        Guest save = guestRepository.save(guest);
        return mapper.convertValue(save, GuestInfoResponse.class);
    }


    public GuestInfoResponse getGuest(Long id) {
        Guest guest = getGuestFromDB(id);
        return mapper.convertValue(guest, GuestInfoResponse.class);
    }

    public Guest getGuestFromDB(Long id) {
        return guestRepository.findById(id).orElseThrow(() -> new CustomException("Guest not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteGuest(Long id) {
        Guest guest = getGuestFromDB(id);
        guest.setUpdatedAt(LocalDateTime.now());
        guest.setStatus(GuestStatus.DELETED);
        guestRepository.save(guest);

    }

    public Page<GuestInfoResponse> getAllGuests(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {


        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Guest> all;
        if (filter == null) {
            all = guestRepository.findAllByStatusNot(pageRequest, GuestStatus.DELETED);
        } else {
            all = guestRepository.findAllByStatusNotFiltered(pageRequest, GuestStatus.DELETED, filter.toLowerCase());
        }

        List<GuestInfoResponse> content = all.getContent().stream()
                .map(guest -> mapper.convertValue(guest, GuestInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }
}
