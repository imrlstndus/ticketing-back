package com.ticketing.common;

import com.ticketing.event.Event;
import com.ticketing.event.EventRepository;
import com.ticketing.seat.Seat;
import com.ticketing.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (eventRepository.count() > 0) return;

        Event event = eventRepository.save(new Event(
                "2026 겨울 콘서트", "올림픽홀",
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().minusMinutes(1)));

        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'E'; row++) {
            int price = row <= 'B' ? 150_000 : 110_000;
            for (int n = 1; n <= 10; n++) {
                seats.add(new Seat(event, String.valueOf(row), n, price));
            }
        }
        seatRepository.saveAll(seats);
    }
}
