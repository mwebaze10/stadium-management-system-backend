package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.EventType;
import auca.ac.rw.stadiumManagement.domain.VenueManager;
import auca.ac.rw.stadiumManagement.domain.Zone;
import auca.ac.rw.stadiumManagement.repository.EventTypeRepository;
import auca.ac.rw.stadiumManagement.repository.VenueManagerRepository;
import auca.ac.rw.stadiumManagement.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueManagerService {

    @Autowired
    private VenueManagerRepository venueManagerRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    public String save(VenueManager venueManager) {
        venueManagerRepository.save(venueManager);
        return "Venue manager saved successfully";
    }

    public List<VenueManager> getAll() {
        return venueManagerRepository.findAll();
    }

    public Optional<VenueManager> getById(UUID id) {
        return venueManagerRepository.findById(id);
    }

    public String update(UUID id, VenueManager updated) {
        Optional<VenueManager> existing = venueManagerRepository.findById(id);
        if (existing.isEmpty()) return "The venue manager with that id does not exist";
        VenueManager manager = existing.get();
        manager.setFirstName(updated.getFirstName());
        manager.setLastName(updated.getLastName());
        manager.setPhone(updated.getPhone());
        manager.setZone(updated.getZone());
        venueManagerRepository.save(manager);
        return "Venue manager updated successfully";
    }

    public String delete(UUID id) {
        if (!venueManagerRepository.existsById(id)) return "The venue manager with that id does not exist";
        venueManagerRepository.deleteById(id);
        return "Venue manager deleted successfully";
    }

    public List<VenueManager> getByEventType(String name) {
        return venueManagerRepository.findByEventTypeName(name);
    }

    public String addEventType(UUID venueManagerId, UUID eventTypeId) {
        Optional<VenueManager> vmOpt = venueManagerRepository.findById(venueManagerId);
        if (vmOpt.isEmpty()) return "The venue manager with that id does not exist";

        Optional<EventType> etOpt = eventTypeRepository.findById(eventTypeId);
        if (etOpt.isEmpty()) return "That event type does not exist";

        VenueManager manager = vmOpt.get();
        EventType eventType = etOpt.get();

        if (manager.getEventTypes().contains(eventType)) return "The venue manager already has that event type";

        manager.getEventTypes().add(eventType);
        venueManagerRepository.save(manager);
        return "Event type added to the venue manager";
    }

    public String changeZone(UUID venueManagerId, int zoneNumber) {
        Optional<VenueManager> vmOpt = venueManagerRepository.findById(venueManagerId);
        if (vmOpt.isEmpty()) return "The venue manager with that id does not exist";

        Optional<Zone> zoneOpt = zoneRepository.findByZoneNumber(zoneNumber);
        if (zoneOpt.isEmpty()) return "The zone with that zoneNumber does not exist";

        Zone zone = zoneOpt.get();
        VenueManager manager = vmOpt.get();

        if (venueManagerRepository.existsByZone(zone)
                && (manager.getZone() == null || !zone.getId().equals(manager.getZone().getId()))) {
            return "That zone is taken by another venue manager";
        }

        manager.setZone(zone);
        venueManagerRepository.save(manager);
        return "Zone changed successfully";
    }
}
