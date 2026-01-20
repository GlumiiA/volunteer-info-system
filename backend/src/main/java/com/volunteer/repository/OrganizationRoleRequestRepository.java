package com.volunteer.repository;

import com.volunteer.entity.OrganizationRoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRoleRequestRepository extends JpaRepository<OrganizationRoleRequest, Integer> {
    List<OrganizationRoleRequest> findByUserId(Integer userId);
    Optional<OrganizationRoleRequest> findByUserIdAndStatus(Integer userId, OrganizationRoleRequest.RequestStatus status);
    List<OrganizationRoleRequest> findByStatus(OrganizationRoleRequest.RequestStatus status);
}
