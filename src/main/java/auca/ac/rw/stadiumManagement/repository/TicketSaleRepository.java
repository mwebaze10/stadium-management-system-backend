package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.domain.TicketSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketSaleRepository extends JpaRepository<TicketSale, UUID> {

    boolean existsByStadiumBooking(StadiumBooking stadiumBooking);

    @Query("SELECT t FROM TicketSale t WHERE t.stadiumBooking.organizer.organizerCode = :organizerCode ORDER BY t.saleDate DESC")
    List<TicketSale> findByOrganizerCode(String organizerCode);
}
