package com.ticketing.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String venue;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    /** 예매 오픈 시각 — 이 시각 전에는 예약 불가 */
    @Column(nullable = false)
    private LocalDateTime openAt;

    public Event(String title, String venue, LocalDateTime startsAt, LocalDateTime openAt) {
        this.title = title;
        this.venue = venue;
        this.startsAt = startsAt;
        this.openAt = openAt;
    }

    public boolean isOpen(LocalDateTime now) {
        return !now.isBefore(openAt);
    }
}
