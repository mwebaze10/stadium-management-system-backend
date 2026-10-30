package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.Organizer;
import auca.ac.rw.stadiumManagement.repository.OrganizerRepository;
import auca.ac.rw.stadiumManagement.repository.VenueManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private VenueManagerRepository venueManagerRepository;

    public String save(Organizer organizer) {
        organizerRepository.save(organizer);
        return "Organizer saved successfully";
    }

    public List<Organizer> getAll() {
        return organizerRepository.findAll();
    }

    public Optional<Organizer> getById(UUID id) {
        return organizerRepository.findById(id);
    }

    public String update(UUID id, Organizer updated) {
        Optional<Organizer> existing = organizerRepository.findById(id);
        if (existing.isEmpty()) return "The organizer with that id does not exist";
        Organizer organizer = existing.get();
        organizer.setOrganizationName(updated.getOrganizationName());
        organizer.setContactPerson(updated.getContactPerson());
        organizer.setOrganizerCode(updated.getOrganizerCode());
        organizer.setPhone(updated.getPhone());
        organizer.setEmail(updated.getEmail());
        organizerRepository.save(organizer);
        return "Organizer updated successfully";
    }

    public String delete(UUID id) {
        if (!organizerRepository.existsById(id)) return "The organizer with that id does not exist";
        organizerRepository.deleteById(id);
        return "Organizer deleted successfully";
    }

    public List<Organizer> getByOrganizationName(String organizationName) {
        return organizerRepository.findByOrganizationNameIgnoreCaseOrderByContactPersonAsc(organizationName);
    }

    public Optional<List<Organizer>> getByVenueManager(UUID venueManagerId) {
        if (!venueManagerRepository.existsById(venueManagerId)) {
            return Optional.empty();
        }
        return Optional.of(organizerRepository.findByVenueManagerId(venueManagerId));
    }
}
