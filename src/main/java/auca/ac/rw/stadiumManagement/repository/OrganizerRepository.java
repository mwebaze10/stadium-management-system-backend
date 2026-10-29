package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, UUID> {

    List<Organizer> findByOrganizationNameIgnoreCaseOrderByContactPersonAsc(String organizationName);

    @Query("SELECT DISTINCT b.organizer FROM StadiumBooking b WHERE b.venueManager.id = :venueManagerId")
    List<Organizer> findByVenueManagerId(UUID venueManagerId);
}
