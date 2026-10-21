package com.ticketing.reservation;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(@NotNull Long seatId) {
}
