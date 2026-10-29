package auca.ac.rw.stadiumManagement.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "venue_manager")
public class VenueManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToMany
    @JoinTable(
        name = "venue_manager_event_type",
        joinColumns = @JoinColumn(name = "venue_manager_id"),
        inverseJoinColumns = @JoinColumn(name = "event_type_id")
    )
    private List<EventType> eventTypes = new ArrayList<>();

    public VenueManager() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public List<EventType> getEventTypes() { return eventTypes; }
    public void setEventTypes(List<EventType> eventTypes) { this.eventTypes = eventTypes; }
}
