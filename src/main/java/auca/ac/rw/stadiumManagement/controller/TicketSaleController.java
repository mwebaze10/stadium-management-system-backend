package auca.ac.rw.stadiumManagement.controller;

import auca.ac.rw.stadiumManagement.domain.TicketSale;
import auca.ac.rw.stadiumManagement.service.TicketSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket-sales")
public class TicketSaleController {

    @Autowired
    private TicketSaleService ticketSaleService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID stadiumBookingId, @RequestBody TicketSale ticketSale) {
        String message = ticketSaleService.save(stadiumBookingId, ticketSale);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        if (message.contains("only for HELD")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ticketSaleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<TicketSale> result = ticketSaleService.getById(id);
        if (result.isPresent()) return ResponseEntity.ok(result.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ticket sale with that id does not exist");
    }

    @GetMapping("/organizer/{organizerCode}")
    public ResponseEntity<?> getByOrganizer(@PathVariable String organizerCode) {
        return ResponseEntity.ok(ticketSaleService.getByOrganizerCode(organizerCode));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String message = ticketSaleService.delete(id);
        if (message.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        return ResponseEntity.ok(message);
    }
}
