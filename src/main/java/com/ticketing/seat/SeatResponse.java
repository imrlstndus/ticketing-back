package com.ticketing.seat;

public record SeatResponse(Long id, String row, int number, int price, SeatStatus status) {
    public static SeatResponse from(Seat s) {
        return new SeatResponse(s.getId(), s.getRow(), s.getNumber(), s.getPrice(), s.getStatus());
    }
}
