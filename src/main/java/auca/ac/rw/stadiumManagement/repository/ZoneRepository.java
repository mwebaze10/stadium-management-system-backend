package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, UUID> {

    Optional<Zone> findByZoneNumber(int zoneNumber);

    @Query("SELECT z FROM Zone z WHERE NOT EXISTS (SELECT v FROM VenueManager v WHERE v.zone = z) ORDER BY z.zoneNumber")
    List<Zone> findFreeZones();
}
