package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.domain.VenueManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface StadiumBookingRepository extends JpaRepository<StadiumBooking, UUID> {

    List<StadiumBooking> findByStatusOrderByEventDateAsc(BookingStatus status);

    List<StadiumBooking> findByEventDateBetweenOrderByEventDateAsc(LocalDate start, LocalDate end);

    boolean existsByVenueManagerAndEventDateAndStatusNot(VenueManager venueManager, LocalDate eventDate, BookingStatus status);

    long countByStatus(BookingStatus status);

    long countByEventDate(LocalDate eventDate);

    @Query("SELECT b.status, COUNT(b) FROM StadiumBooking b GROUP BY b.status")
    List<Object[]> countByStatus();

    @Modifying
    @Query("UPDATE StadiumBooking b SET b.status = :declined WHERE b.venueManager.id = :venueManagerId AND b.eventDate = :date AND b.status <> :held")
    int cancelByVenueManagerAndDate(@Param("venueManagerId") UUID venueManagerId,
                                    @Param("date") LocalDate date,
                                    @Param("declined") BookingStatus declined,
                                    @Param("held") BookingStatus held);
}
