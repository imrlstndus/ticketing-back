package com.ticketing.reservation;

import com.ticketing.event.Event;
import com.ticketing.event.EventRepository;
import com.ticketing.seat.Seat;
import com.ticketing.seat.SeatRepository;
import com.ticketing.seat.SeatStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationConcurrencyTest {

    @Autowired ReservationService reservationService;
    @Autowired ReservationRepository reservationRepository;
    @Autowired SeatRepository seatRepository;
    @Autowired EventRepository eventRepository;

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAll();
        seatRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void 같은_좌석에_100명이_동시에_예약하면_1명만_성공한다() throws InterruptedException {
        Event event = eventRepository.save(new Event("테스트 공연", "홀",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().minusMinutes(1)));
        Seat seat = seatRepository.save(new Seat(event, "A", 1, 100_000));

        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threads);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            long userId = i + 1;
            pool.submit(() -> {
                try {
                    reservationService.reserve(userId, seat.getId());
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        pool.shutdown();

        assertThat(success.get()).isEqualTo(1);
        assertThat(fail.get()).isEqualTo(threads - 1);
        assertThat(reservationRepository.count()).isEqualTo(1);
        assertThat(seatRepository.findById(seat.getId()).orElseThrow().getStatus())
                .isEqualTo(SeatStatus.RESERVED);
    }
}
