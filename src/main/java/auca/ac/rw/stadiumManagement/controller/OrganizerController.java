package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.Organizer;
import auca.ac.rw.stadiumManagement.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Organizer organizer) {
        String message = organizerService.save(organizer);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(organizerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Organizer> result = organizerService.getById(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The organizer with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Organizer organizer) {
        String message = organizerService.update(id, organizer);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = organizerService.delete(id);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-organization-name")
    public ResponseEntity<?> getByOrganizationName(@RequestParam String organizationName) {
        return ResponseEntity.ok(organizerService.getByOrganizationName(organizationName));
    }

    @GetMapping("/of-venue-manager/{venueManagerId}")
    public ResponseEntity<?> getByVenueManager(@PathVariable UUID venueManagerId) {
        Optional<List<Organizer>> result = organizerService.getByVenueManager(venueManagerId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The venue manager with that id does not exist");
        }
        return ResponseEntity.ok(result.get());
    }
}
