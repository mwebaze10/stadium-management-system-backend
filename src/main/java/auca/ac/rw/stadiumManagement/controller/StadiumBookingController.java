package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.BookingStatus;
import auca.ac.rw.stadiumManagement.domain.StadiumBooking;
import auca.ac.rw.stadiumManagement.service.StadiumBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/stadium-bookings")
public class StadiumBookingController {

    @Autowired
    private StadiumBookingService stadiumBookingService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody StadiumBooking booking) {
        String message = stadiumBookingService.save(booking);
        if (message.contains("in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (message.contains("already supervises")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(stadiumBookingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<StadiumBooking> result = stadiumBookingService.getById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The stadium booking with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody StadiumBooking booking) {
        String message = stadiumBookingService.update(id, booking);
        if (message.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = stadiumBookingService.delete(id);
        if (message.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.contains("ticket sales")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-status")
    public ResponseEntity<?> getByStatus(@RequestParam BookingStatus status) {
        return ResponseEntity.ok(stadiumBookingService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<?> getBetween(@RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(stadiumBookingService.getBetween(LocalDate.parse(start), LocalDate.parse(end)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam BookingStatus status) {
        String message = stadiumBookingService.updateStatus(id, status);
        if (message.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.contains("Cannot change")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<?> getStatsByStatus() {
        return ResponseEntity.ok(stadiumBookingService.getStatsByStatus());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID venueManagerId, @RequestParam String date) {
        return ResponseEntity.ok(stadiumBookingService.cancelDay(venueManagerId, LocalDate.parse(date)));
    }

    @GetMapping("/page")
    public Page<StadiumBooking> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "eventDate,desc") String sort) {
        String[] sortParts = sort.split(",", 2);
        String property = sortParts[0];
        Sort.Direction direction = sortParts.length == 2
                ? Sort.Direction.fromString(sortParts[1])
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));
        return stadiumBookingService.getPage(pageable);
    }
}
