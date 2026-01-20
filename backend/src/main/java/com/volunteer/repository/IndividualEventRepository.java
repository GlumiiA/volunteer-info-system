package com.volunteer.repository;

import com.volunteer.entity.IndividualEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndividualEventRepository extends JpaRepository<IndividualEvent, Integer> {
    List<IndividualEvent> findByOrganisationId(Integer organisationId);
}
