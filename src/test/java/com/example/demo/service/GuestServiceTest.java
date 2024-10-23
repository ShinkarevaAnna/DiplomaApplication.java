package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.Guest;
import com.example.demo.model.db.repository.GuestRepository;
import com.example.demo.model.dto.request.GuestInfoRequest;
import com.example.demo.model.dto.response.GuestInfoResponse;
import com.example.demo.model.enums.GuestStatus;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GuestServiceTest {
    @InjectMocks
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createGuest() {
        GuestInfoRequest request = new GuestInfoRequest();

        Guest guest = new Guest();
        guest.setId(1L);

        when(guestRepository.save(any(Guest.class))).thenReturn(guest);

        GuestInfoResponse result = guestService.createGuest(request);

        assertEquals(guest.getId(), result.getId());
    }

    @Test
    public void getGuest() {
        Guest guest = new Guest();
        guest.setId(1L);
        when(guestRepository.findById(guest.getId())).thenReturn(Optional.of(guest));
        GuestInfoResponse result = guestService.getGuest(1L);
        GuestInfoResponse expected = mapper.convertValue(guest, GuestInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test(expected = CustomException.class)
    public void getUser_noUser() {
        Guest guest = new Guest();
        guest.setId(1L);
        GuestInfoResponse result = guestService.getGuest(2L);
        GuestInfoResponse expected = mapper.convertValue(guest, GuestInfoResponse.class);
        assertEquals(expected, result);
    }

    @Test
    public void deleteGuest() {
        Guest guest = new Guest();
        guest.setId(1L);

        when(guestRepository.findById(guest.getId())).thenReturn(Optional.of(guest));

        guestService.deleteGuest(guest.getId());

        verify(guestRepository, times(1)).save(any(Guest.class));
        assertEquals(GuestStatus.DELETED, guest.getStatus());
    }

    @Test
    public void getAllGuests() {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Guest guest = new Guest();
        guest.setId(1L);
        Guest guest1 = new Guest();
        guest1.setId(2L);
        List<Guest> guests = List.of(guest, guest1);
        Page<Guest> guestsPage = new PageImpl<>(guests, pageRequest, guests.size());
        when(guestRepository.findAllByStatusNot(pageRequest, GuestStatus.DELETED)).thenReturn(guestsPage);
        Page<GuestInfoResponse> result = guestService.getAllGuests(0, 10, "id", Sort.Direction.ASC, null);
        assertEquals(2, result.getTotalElements());
        assertEquals(guest.getId(), result.getContent().get(0).getId());
        assertEquals(guest1.getId(), result.getContent().get(1).getId());
    }
}