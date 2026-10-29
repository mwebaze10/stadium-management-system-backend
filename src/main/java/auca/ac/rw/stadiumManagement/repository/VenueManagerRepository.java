package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.VenueManager;
import auca.ac.rw.stadiumManagement.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueManagerRepository extends JpaRepository<VenueManager, UUID> {

    boolean existsByZone(Zone zone);

    @Query("SELECT v FROM VenueManager v JOIN v.eventTypes e WHERE LOWER(e.name) = LOWER(:name)")
    List<VenueManager> findByEventTypeName(String name);
}
