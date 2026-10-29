package auca.ac.rw.stadiumManagement.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int zoneNumber;
    private String name;

    public Zone() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public int getZoneNumber() { return zoneNumber; }
    public void setZoneNumber(int zoneNumber) { this.zoneNumber = zoneNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
