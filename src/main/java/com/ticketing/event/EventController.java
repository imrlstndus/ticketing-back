package com.ticketing.event;

import com.ticketing.common.BusinessException;
import com.ticketing.common.ErrorCode;
import com.ticketing.seat.SeatRepository;
import com.ticketing.seat.SeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventController {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @GetMapping
    public List<EventResponse> list() {
        return eventRepository.findAll().stream().map(EventResponse::from).toList();
    }

    @GetMapping("/{eventId}")
    public EventResponse get(@PathVariable Long eventId) {
        return eventRepository.findById(eventId).map(EventResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
    }

    @GetMapping("/{eventId}/seats")
    public List<SeatResponse> seats(@PathVariable Long eventId) {
        return seatRepository.findByEventIdOrderByRowAscNumberAsc(eventId).stream()
                .map(SeatResponse::from).toList();
    }
}
