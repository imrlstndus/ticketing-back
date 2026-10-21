package com.ticketing.reservation;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id, Long seatId, String seatLabel, int price,
        ReservationStatus status, LocalDateTime createdAt) {

    public static ReservationResponse from(Reservation r) {
        var s = r.getSeat();
        return new ReservationResponse(r.getId(), s.getId(), s.getRow() + s.getNumber(),
                s.getPrice(), r.getStatus(), r.getCreatedAt());
    }
}
