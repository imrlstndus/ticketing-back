package com.ticketing.event;

import java.time.LocalDateTime;

public record EventResponse(Long id, String title, String venue, LocalDateTime startsAt, LocalDateTime openAt) {
    public static EventResponse from(Event e) {
        return new EventResponse(e.getId(), e.getTitle(), e.getVenue(), e.getStartsAt(), e.getOpenAt());
    }
}
