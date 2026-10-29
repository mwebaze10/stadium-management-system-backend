package auca.ac.rw.stadiumManagement.repository;

import auca.ac.rw.stadiumManagement.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, UUID> {

    @Query("SELECT e FROM EventType e WHERE e.venueManagers IS EMPTY")
    List<EventType> findUnused();
}
