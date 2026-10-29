package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.EventType;
import auca.ac.rw.stadiumManagement.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody EventType eventType) {
        String message = eventTypeService.save(eventType);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(eventTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<EventType> result = eventTypeService.getById(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The event type with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody EventType eventType) {
        String message = eventTypeService.update(id, eventType);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = eventTypeService.delete(id);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/unused")
    public ResponseEntity<?> getUnused() {
        return ResponseEntity.ok(eventTypeService.getUnused());
    }
}
