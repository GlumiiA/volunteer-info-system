package com.volunteer.repository;

import com.volunteer.entity.MassEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MassEventRepository extends JpaRepository<MassEvent, Integer> {
    List<MassEvent> findByOrganisationId(Integer organisationId);
}
