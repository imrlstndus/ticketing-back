package com.ticketing.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "공연을 찾을 수 없습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    SEAT_ALREADY_TAKEN(HttpStatus.CONFLICT, "이미 선택된 좌석입니다."),
    NOT_OPEN_YET(HttpStatus.BAD_REQUEST, "아직 예매 오픈 전입니다.");

    private final HttpStatus status;
    private final String message;
}
