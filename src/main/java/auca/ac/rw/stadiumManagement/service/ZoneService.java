package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.Zone;
import auca.ac.rw.stadiumManagement.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public String save(Zone zone) {
        zoneRepository.save(zone);
        return "Zone saved successfully";
    }

    public List<Zone> getAll() {
        return zoneRepository.findAll();
    }

    public Optional<Zone> getById(UUID id) {
        return zoneRepository.findById(id);
    }

    public String update(UUID id, Zone updated) {
        Optional<Zone> existing = zoneRepository.findById(id);
        if (existing.isEmpty()) return "The zone with that id does not exist";
        Zone zone = existing.get();
        zone.setZoneNumber(updated.getZoneNumber());
        zone.setName(updated.getName());
        zoneRepository.save(zone);
        return "Zone updated successfully";
    }

    public String delete(UUID id) {
        if (!zoneRepository.existsById(id)) return "The zone with that id does not exist";
        zoneRepository.deleteById(id);
        return "Zone deleted successfully";
    }

    public List<Zone> getFreeZones() {
        return zoneRepository.findFreeZones();
    }
}
