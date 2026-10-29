package auca.ac.rw.stadiumManagement.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ticket_sale")
public class TicketSale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String ticketCategory;
    private String remarks;
    private int ticketsSold;
    private LocalDate saleDate;

    @ManyToOne
    @JoinColumn(name = "stadium_booking_id")
    private StadiumBooking stadiumBooking;

    public TicketSale() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTicketCategory() { return ticketCategory; }
    public void setTicketCategory(String ticketCategory) { this.ticketCategory = ticketCategory; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public int getTicketsSold() { return ticketsSold; }
    public void setTicketsSold(int ticketsSold) { this.ticketsSold = ticketsSold; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }

    public StadiumBooking getStadiumBooking() { return stadiumBooking; }
    public void setStadiumBooking(StadiumBooking stadiumBooking) { this.stadiumBooking = stadiumBooking; }
}
