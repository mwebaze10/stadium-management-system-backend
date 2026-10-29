package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.Zone;
import auca.ac.rw.stadiumManagement.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Zone zone) {
        String message = zoneService.save(zone);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(zoneService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Zone> result = zoneService.getById(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The zone with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Zone zone) {
        String message = zoneService.update(id, zone);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = zoneService.delete(id);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/free")
    public ResponseEntity<?> getFreeZones() {
        return ResponseEntity.ok(zoneService.getFreeZones());
    }
}
