package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.VenueManager;
import auca.ac.rw.stadiumManagement.service.VenueManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/venue-managers")
public class VenueManagerController {

    @Autowired
    private VenueManagerService venueManagerService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody VenueManager venueManager) {
        String message = venueManagerService.save(venueManager);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(venueManagerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<VenueManager> result = venueManagerService.getById(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The venue manager with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody VenueManager venueManager) {
        String message = venueManagerService.update(id, venueManager);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = venueManagerService.delete(id);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-event-type")
    public ResponseEntity<?> getByEventType(@RequestParam String name) {
        return ResponseEntity.ok(venueManagerService.getByEventType(name));
    }

    @PutMapping("/{venueManagerId}/event-types/{eventTypeId}")
    public ResponseEntity<?> addEventType(@PathVariable UUID venueManagerId, @PathVariable UUID eventTypeId) {
        String message = venueManagerService.addEventType(venueManagerId, eventTypeId);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        if (message.contains("already has")) return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{venueManagerId}/zone")
    public ResponseEntity<?> changeZone(@PathVariable UUID venueManagerId, @RequestParam int zoneNumber) {
        String message = venueManagerService.changeZone(venueManagerId, zoneNumber);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        if (message.contains("taken")) return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        return ResponseEntity.ok(message);
    }
}
