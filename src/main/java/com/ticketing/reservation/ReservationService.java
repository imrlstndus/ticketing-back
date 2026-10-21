package com.ticketing.reservation;

import com.ticketing.common.BusinessException;
import com.ticketing.common.ErrorCode;
import com.ticketing.seat.Seat;
import com.ticketing.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse reserve(Long userId, Long seatId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SEAT_NOT_FOUND));

        if (!seat.getEvent().isOpen(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.NOT_OPEN_YET);
        }

        seat.reserve();
        Reservation saved = reservationRepository.save(new Reservation(userId, seat));
        return ReservationResponse.from(saved);
    }

    @Transactional
    public void cancel(Long userId, Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .filter(res -> res.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
        r.cancel();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> myReservations(Long userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReservationResponse::from).toList();
    }
}
