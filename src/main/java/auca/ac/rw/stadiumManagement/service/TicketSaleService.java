package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.domain.TicketSale;
import auca.ac.rw.stadiumManagement.repository.StadiumBookingRepository;
import auca.ac.rw.stadiumManagement.repository.TicketSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketSaleService {

    @Autowired
    private TicketSaleRepository ticketSaleRepository;

    @Autowired
    private StadiumBookingRepository stadiumBookingRepository;

    public String save(UUID stadiumBookingId, TicketSale ticketSale) {
        Optional<StadiumBooking> opt = stadiumBookingRepository.findById(stadiumBookingId);
        if (opt.isEmpty()) return "The stadium booking with that id does not exist";

        StadiumBooking booking = opt.get();
        if (booking.getStatus() != BookingStatus.HELD) return "Ticket sales are only for HELD stadium bookings";

        ticketSale.setSaleDate(LocalDate.now());
        ticketSale.setStadiumBooking(booking);
        ticketSaleRepository.save(ticketSale);
        return "Ticket sale saved successfully";
    }

    public List<TicketSale> getAll() {
        return ticketSaleRepository.findAll();
    }

    public Optional<TicketSale> getById(UUID id) {
        return ticketSaleRepository.findById(id);
    }

    public List<TicketSale> getByOrganizerCode(String organizerCode) {
        return ticketSaleRepository.findByOrganizerCode(organizerCode);
    }

    public String delete(UUID id) {
        if (!ticketSaleRepository.existsById(id)) return "The ticket sale with that id does not exist";
        ticketSaleRepository.deleteById(id);
        return "Ticket sale deleted successfully";
    }
}
