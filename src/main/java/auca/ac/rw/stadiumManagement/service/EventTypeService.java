package auca.ac.rw.stadiumManagement.service;

import auca.ac.rw.stadiumManagement.domain.EventType;
import auca.ac.rw.stadiumManagement.repository.EventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventTypeService {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    public String save(EventType eventType) {
        eventTypeRepository.save(eventType);
        return "Event type saved successfully";
    }

    public List<EventType> getAll() {
        return eventTypeRepository.findAll();
    }

    public Optional<EventType> getById(UUID id) {
        return eventTypeRepository.findById(id);
    }

    public String update(UUID id, EventType updated) {
        Optional<EventType> existing = eventTypeRepository.findById(id);
        if (existing.isEmpty()) return "The event type with that id does not exist";
        EventType eventType = existing.get();
        eventType.setName(updated.getName());
        eventTypeRepository.save(eventType);
        return "Event type updated successfully";
    }

    public String delete(UUID id) {
        if (!eventTypeRepository.existsById(id)) return "The event type with that id does not exist";
        eventTypeRepository.deleteById(id);
        return "Event type deleted successfully";
    }

    public List<EventType> getUnused() {
        return eventTypeRepository.findUnused();
    }
}
