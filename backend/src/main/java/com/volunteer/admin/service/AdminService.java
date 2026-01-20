package com.volunteer.admin.service;

import com.volunteer.admin.dto.UpdateUserRoleRequest;
import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.service.CurrentUserService;
import com.volunteer.entity.*;
import com.volunteer.event.service.EventService;
import com.volunteer.organization.dto.CreateOrganizationRequest;
import com.volunteer.organization.dto.OrganizationRoleRequestItem;
import com.volunteer.repository.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationRoleRequestRepository organizationRoleRequestRepository;
    private final EventService eventService;

    public AdminService(
            CurrentUserService currentUserService,
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            OrganizationRoleRequestRepository organizationRoleRequestRepository,
            EventService eventService) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.organizationRoleRequestRepository = organizationRoleRequestRepository;
        this.eventService = eventService;
    }

    private void checkAdminRole() {
        User user = currentUserService.getCurrentUser();
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only administrators can perform this action");
        }
    }

    @Transactional
    public User updateUserRole(Integer userId, UpdateUserRoleRequest request) {
        checkAdminRole();
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserRole newRole = UserRole.valueOf(request.getRole().name());
        
        if (newRole == UserRole.ORG_REPRESENTATIVE) {
            if (request.getOrganisationId() == null || !request.getOrganisationId().isPresent()) {
                throw new RuntimeException("Organization ID is required for ORG_REPRESENTATIVE role");
            }
            Integer orgId = request.getOrganisationId().get();
            organizationRepository.findById(orgId)
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            user.setOrganisationId(orgId);
        } else {
            user.setOrganisationId(null);
        }

        user.setRole(newRole);
        return userRepository.save(user);
    }

    public List<Organization> getAllOrganizations() {
        checkAdminRole();
        return organizationRepository.findAll();
    }

    @Transactional
    public Organization createOrganization(CreateOrganizationRequest request) {
        checkAdminRole();
        
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setDescription(request.getDescription() != null && request.getDescription().isPresent() 
                ? request.getDescription().get() : null);
        organization.setAddress(request.getAddress() != null && request.getAddress().isPresent() 
                ? request.getAddress().get() : null);
        
        return organizationRepository.save(organization);
    }

    @Transactional
    public Organization updateOrganization(Integer id, CreateOrganizationRequest request) {
        checkAdminRole();
        
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        organization.setName(request.getName());
        organization.setDescription(request.getDescription() != null && request.getDescription().isPresent() 
                ? request.getDescription().get() : null);
        organization.setAddress(request.getAddress() != null && request.getAddress().isPresent() 
                ? request.getAddress().get() : null);
        
        return organizationRepository.save(organization);
    }

    @Transactional
    public void deleteOrganization(Integer id) {
        checkAdminRole();
        
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // Check if organization has representatives
        List<User> orgUsers = userRepository.findByOrganisationId(id);
        if (orgUsers.stream().anyMatch(u -> u.getRole() == UserRole.ORG_REPRESENTATIVE)) {
            throw new RuntimeException("Cannot delete organization with representatives");
        }
        
        organizationRepository.delete(organization);
    }

    public List<OrganizationRoleRequestItem> getOrganizationRequests(String status) {
        checkAdminRole();
        
        List<OrganizationRoleRequest> requests;
        if ("ALL".equalsIgnoreCase(status)) {
            requests = organizationRoleRequestRepository.findAll();
        } else {
            OrganizationRoleRequest.RequestStatus requestStatus = 
                    OrganizationRoleRequest.RequestStatus.valueOf(status.toUpperCase());
            requests = organizationRoleRequestRepository.findByStatus(requestStatus);
        }
        
        return requests.stream().map(this::convertToRequestItem).collect(Collectors.toList());
    }

    @Transactional
    public void approveOrganizationRequest(Integer requestId) {
        checkAdminRole();
        
        OrganizationRoleRequest request = organizationRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Organization role request not found"));
        
        if (request.getStatus() != OrganizationRoleRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() == UserRole.ORG_REPRESENTATIVE) {
            throw new RuntimeException("User is already an organization representative");
        }
        
        Organization organization;
        if (request.getRequestType() == OrganizationRoleRequest.RequestType.NEW) {
            // Create new organization
            organization = new Organization();
            organization.setName(request.getOrganizationName());
            organization.setDescription(request.getOrganizationDescription());
            organization.setAddress(request.getOrganizationAddress());
            organization = organizationRepository.save(organization);
        } else {
            // Use existing organization
            organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
        }
        
        // Update user role
        user.setRole(UserRole.ORG_REPRESENTATIVE);
        user.setOrganisationId(organization.getId());
        userRepository.save(user);
        
        // Update request status
        request.setStatus(OrganizationRoleRequest.RequestStatus.APPROVED);
        organizationRoleRequestRepository.save(request);
    }

    @Transactional
    public void rejectOrganizationRequest(Integer requestId) {
        checkAdminRole();
        
        OrganizationRoleRequest request = organizationRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Organization role request not found"));
        
        if (request.getStatus() != OrganizationRoleRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }
        
        request.setStatus(OrganizationRoleRequest.RequestStatus.REJECTED);
        organizationRoleRequestRepository.save(request);
    }

    @Transactional
    public void deleteMassEvent(Integer id) {
        // EventService already checks if user is admin or event owner
        // No need to restrict here - let EventService handle authorization
        eventService.deleteMassEvent(id);
    }

    @Transactional
    public void deleteIndividualEvent(Integer id) {
        // EventService already checks if user is admin or event owner
        // No need to restrict here - let EventService handle authorization
        eventService.deleteIndividualEvent(id);
    }

    private OrganizationRoleRequestItem convertToRequestItem(OrganizationRoleRequest request) {
        OrganizationRoleRequestItem item = new OrganizationRoleRequestItem();
        item.setId(request.getId());
        item.setUserId(request.getUserId());
        
        User user = userRepository.findById(request.getUserId()).orElse(null);
        item.setUser(user);
        
        item.setRequestType(com.volunteer.organization.dto.OrganizationRoleRequestItem.RequestTypeEnum
                .fromValue(request.getRequestType().name()));
        item.setOrganizationId(request.getOrganizationId() != null 
                ? JsonNullable.of(request.getOrganizationId()) 
                : JsonNullable.<Integer>undefined());
        item.setOrganizationName(request.getOrganizationName() != null 
                ? JsonNullable.of(request.getOrganizationName()) 
                : JsonNullable.<String>undefined());
        item.setOrganizationDescription(request.getOrganizationDescription() != null 
                ? JsonNullable.of(request.getOrganizationDescription()) 
                : JsonNullable.<String>undefined());
        item.setOrganizationAddress(request.getOrganizationAddress() != null 
                ? JsonNullable.of(request.getOrganizationAddress()) 
                : JsonNullable.<String>undefined());
        item.setStatus(OrganizationRoleRequestItem.StatusEnum.fromValue(request.getStatus().name()));
        item.setCreatedAt(request.getCreatedAt());
        
        return item;
    }
}
