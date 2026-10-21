package com.ticketing.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse reserve(@RequestHeader("X-User-Id") Long userId,
                                       @Valid @RequestBody ReservationRequest req) {
        return reservationService.reserve(userId, req.seatId());
    }

    @GetMapping("/me")
    public List<ReservationResponse> mine(@RequestHeader("X-User-Id") Long userId) {
        return reservationService.myReservations(userId);
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@RequestHeader("X-User-Id") Long userId, @PathVariable Long reservationId) {
        reservationService.cancel(userId, reservationId);
    }
}
