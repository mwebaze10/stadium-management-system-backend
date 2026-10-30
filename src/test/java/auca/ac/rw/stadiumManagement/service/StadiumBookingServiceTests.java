package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.domain.VenueManager;
import auca.ac.rw.stadiumManagement.repository.StadiumBookingRepository;
import auca.ac.rw.stadiumManagement.repository.TicketSaleRepository;
import auca.ac.rw.stadiumManagement.repository.VenueManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StadiumBookingServiceTests {

    @Mock
    private StadiumBookingRepository stadiumBookingRepository;

    @Mock
    private VenueManagerRepository venueManagerRepository;

    @Mock
    private TicketSaleRepository ticketSaleRepository;

    @InjectMocks
    private StadiumBookingService stadiumBookingService;

    @Test
    void saveRejectsPastEventDate() {
        StadiumBooking booking = new StadiumBooking();
        booking.setEventDate(LocalDate.now().minusDays(1));

        String result = stadiumBookingService.save(booking);

        assertEquals("Stadium booking date cannot be in the past", result);
        verify(stadiumBookingRepository, never()).save(any(StadiumBooking.class));
    }

    @Test
    void saveRejectsManagerDateConflict() {
        UUID managerId = UUID.randomUUID();
        VenueManager manager = new VenueManager();
        manager.setId(managerId);
        StadiumBooking booking = bookingOn(LocalDate.now(), manager);
        when(venueManagerRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(stadiumBookingRepository.existsByVenueManagerAndEventDateAndStatusNot(
                manager, booking.getEventDate(), BookingStatus.DECLINED)).thenReturn(true);

        String result = stadiumBookingService.save(booking);

        assertEquals("Venue manager already supervises an event on that date", result);
        verify(stadiumBookingRepository, never()).save(any(StadiumBooking.class));
    }

    @Test
    void saveStartsNewBookingAsRequested() {
        UUID managerId = UUID.randomUUID();
        VenueManager manager = new VenueManager();
        manager.setId(managerId);
        StadiumBooking booking = bookingOn(LocalDate.now(), manager);
        when(venueManagerRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(stadiumBookingRepository.existsByVenueManagerAndEventDateAndStatusNot(
                manager, booking.getEventDate(), BookingStatus.DECLINED)).thenReturn(false);

        String result = stadiumBookingService.save(booking);

        assertEquals("Stadium booking saved successfully", result);
        assertEquals(BookingStatus.REQUESTED, booking.getStatus());
        verify(stadiumBookingRepository).save(booking);
    }

    @Test
    void updateStatusAllowsRequestedToApprovedAndApprovedToHeld() {
        UUID bookingId = UUID.randomUUID();
        StadiumBooking booking = new StadiumBooking();
        booking.setStatus(BookingStatus.REQUESTED);
        when(stadiumBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertEquals("Stadium booking status updated successfully",
                stadiumBookingService.updateStatus(bookingId, BookingStatus.APPROVED));
        assertEquals(BookingStatus.APPROVED, booking.getStatus());

        assertEquals("Stadium booking status updated successfully",
                stadiumBookingService.updateStatus(bookingId, BookingStatus.HELD));
        assertEquals(BookingStatus.HELD, booking.getStatus());
        verify(stadiumBookingRepository, times(2)).save(booking);
    }

    @Test
    void updateStatusRejectsTransitionOutOfHeld() {
        UUID bookingId = UUID.randomUUID();
        StadiumBooking booking = new StadiumBooking();
        booking.setStatus(BookingStatus.HELD);
        when(stadiumBookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertEquals("Cannot change status from HELD to APPROVED",
                stadiumBookingService.updateStatus(bookingId, BookingStatus.APPROVED));
        verify(stadiumBookingRepository, never()).save(booking);
    }

    private StadiumBooking bookingOn(LocalDate eventDate, VenueManager manager) {
        StadiumBooking booking = new StadiumBooking();
        booking.setEventDate(eventDate);
        booking.setVenueManager(manager);
        return booking;
    }
}
