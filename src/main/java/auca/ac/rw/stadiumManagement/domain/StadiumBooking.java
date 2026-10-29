package auca.ac.rw.stadiumManagement.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "stadium_booking")
public class StadiumBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate eventDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String eventTitle;
    private int expectedAttendance;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @ManyToOne
    @JoinColumn(name = "venue_manager_id")
    private VenueManager venueManager;

    public StadiumBooking() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public int getExpectedAttendance() { return expectedAttendance; }
    public void setExpectedAttendance(int expectedAttendance) { this.expectedAttendance = expectedAttendance; }

    public Organizer getOrganizer() { return organizer; }
    public void setOrganizer(Organizer organizer) { this.organizer = organizer; }

    public VenueManager getVenueManager() { return venueManager; }
    public void setVenueManager(VenueManager venueManager) { this.venueManager = venueManager; }
}
