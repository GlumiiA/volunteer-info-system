package com.volunteer.repository;

import com.volunteer.entity.VolunteerBookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerBookEntryRepository extends JpaRepository<VolunteerBookEntry, Integer> {
    List<VolunteerBookEntry> findByUserId(Integer userId);
    List<VolunteerBookEntry> findByEventId(Integer eventId);
}
