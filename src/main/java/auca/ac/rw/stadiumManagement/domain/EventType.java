package auca.ac.rw.stadiumManagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event_type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "eventTypes")
    @JsonIgnore
    private List<VenueManager> venueManagers;

    public EventType() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<VenueManager> getVenueManagers() { return venueManagers; }
    public void setVenueManagers(List<VenueManager> venueManagers) { this.venueManagers = venueManagers; }
}
