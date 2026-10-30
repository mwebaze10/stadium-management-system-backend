package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.repository.OrganizerRepository;
import auca.ac.rw.stadiumManagement.repository.StadiumBookingRepository;
import auca.ac.rw.stadiumManagement.repository.TicketSaleRepository;
import auca.ac.rw.stadiumManagement.repository.VenueManagerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final OrganizerRepository organizerRepository;
    private final VenueManagerRepository venueManagerRepository;
    private final ZoneService zoneService;
    private final StadiumBookingRepository stadiumBookingRepository;
    private final TicketSaleRepository ticketSaleRepository;

    public DashboardService(
            OrganizerRepository organizerRepository,
            VenueManagerRepository venueManagerRepository,
            ZoneService zoneService,
            StadiumBookingRepository stadiumBookingRepository,
            TicketSaleRepository ticketSaleRepository) {
        this.organizerRepository = organizerRepository;
        this.venueManagerRepository = venueManagerRepository;
        this.zoneService = zoneService;
        this.stadiumBookingRepository = stadiumBookingRepository;
        this.ticketSaleRepository = ticketSaleRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("organizers", organizerRepository.count());
        summary.put("venueManagers", venueManagerRepository.count());
        summary.put("freeZones", (long) zoneService.getFreeZones().size());
        summary.put("bookingsToday", stadiumBookingRepository.countByEventDate(LocalDate.now()));
        summary.put("requested", stadiumBookingRepository.countByStatus(BookingStatus.REQUESTED));
        summary.put("ticketSales", ticketSaleRepository.count());
        return summary;
    }
}
