package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.domain.VenueManager;
import auca.ac.rw.stadiumManagement.repository.StadiumBookingRepository;
import auca.ac.rw.stadiumManagement.repository.TicketSaleRepository;
import auca.ac.rw.stadiumManagement.repository.VenueManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StadiumBookingService {

    @Autowired
    private StadiumBookingRepository stadiumBookingRepository;

    @Autowired
    private VenueManagerRepository venueManagerRepository;

    @Autowired
    private TicketSaleRepository ticketSaleRepository;

    public String save(StadiumBooking booking) {
        if (booking.getEventDate() == null || booking.getEventDate().isBefore(LocalDate.now())) {
            return "Stadium booking date cannot be in the past";
        }

        if (booking.getVenueManager() != null && booking.getVenueManager().getId() != null) {
            Optional<VenueManager> vmOpt = venueManagerRepository.findById(booking.getVenueManager().getId());
            if (vmOpt.isPresent()) {
                boolean conflict = stadiumBookingRepository.existsByVenueManagerAndEventDateAndStatusNot(
                        vmOpt.get(), booking.getEventDate(), BookingStatus.DECLINED);
                if (conflict) {
                    return "Venue manager already supervises an event on that date";
                }
            }
        }

        booking.setStatus(BookingStatus.REQUESTED);
        stadiumBookingRepository.save(booking);
        return "Stadium booking saved successfully";
    }

    public List<StadiumBooking> getAll() {
        return stadiumBookingRepository.findAll();
    }

    public Page<StadiumBooking> getPage(Pageable pageable) {
        return stadiumBookingRepository.findAll(pageable);
    }

    public Optional<StadiumBooking> getById(UUID id) {
        return stadiumBookingRepository.findById(id);
    }

    public String update(UUID id, StadiumBooking updated) {
        Optional<StadiumBooking> existing = stadiumBookingRepository.findById(id);
        if (existing.isEmpty()) return "The stadium booking with that id does not exist";
        StadiumBooking booking = existing.get();
        booking.setEventDate(updated.getEventDate());
        booking.setEventTitle(updated.getEventTitle());
        booking.setExpectedAttendance(updated.getExpectedAttendance());
        booking.setOrganizer(updated.getOrganizer());
        booking.setVenueManager(updated.getVenueManager());
        stadiumBookingRepository.save(booking);
        return "Stadium booking updated successfully";
    }

    public String delete(UUID id) {
        Optional<StadiumBooking> opt = stadiumBookingRepository.findById(id);
        if (opt.isEmpty()) return "The stadium booking with that id does not exist";
        if (ticketSaleRepository.existsByStadiumBooking(opt.get())) {
            return "That stadium booking has ticket sales, delete them first";
        }
        stadiumBookingRepository.deleteById(id);
        return "Stadium booking deleted successfully";
    }

    public List<StadiumBooking> getByStatus(BookingStatus status) {
        return stadiumBookingRepository.findByStatusOrderByEventDateAsc(status);
    }

    public List<StadiumBooking> getBetween(LocalDate start, LocalDate end) {
        return stadiumBookingRepository.findByEventDateBetweenOrderByEventDateAsc(start, end);
    }

    public String updateStatus(UUID id, BookingStatus newStatus) {
        Optional<StadiumBooking> opt = stadiumBookingRepository.findById(id);
        if (opt.isEmpty()) return "The stadium booking with that id does not exist";

        StadiumBooking booking = opt.get();
        BookingStatus current = booking.getStatus();

        boolean allowed = (current == BookingStatus.REQUESTED && (newStatus == BookingStatus.APPROVED || newStatus == BookingStatus.DECLINED))
                || (current == BookingStatus.APPROVED && (newStatus == BookingStatus.HELD || newStatus == BookingStatus.DECLINED));

        if (!allowed) return "Cannot change status from " + current + " to " + newStatus;

        booking.setStatus(newStatus);
        stadiumBookingRepository.save(booking);
        return "Stadium booking status updated successfully";
    }

    public List<Object[]> getStatsByStatus() {
        return stadiumBookingRepository.countByStatus();
    }

    @Transactional
    public String cancelDay(UUID venueManagerId, LocalDate date) {
        int count = stadiumBookingRepository.cancelByVenueManagerAndDate(
                venueManagerId, date, BookingStatus.DECLINED, BookingStatus.HELD);
        return count + " stadium bookings cancelled";
    }
}
