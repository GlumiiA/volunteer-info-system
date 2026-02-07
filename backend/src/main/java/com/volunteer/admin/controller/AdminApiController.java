package com.volunteer.admin.controller;

import com.volunteer.admin.dto.UpdateUserRoleRequest;
import com.volunteer.admin.service.AdminService;
import com.volunteer.entity.Organization;
import com.volunteer.entity.User;
import com.volunteer.organization.dto.CreateOrganizationRequest;
import com.volunteer.organization.dto.OrganizationRoleRequestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class AdminApiController implements AdminApi {

    private final NativeWebRequest request;
    private final AdminService adminService;

    @Autowired
    public AdminApiController(NativeWebRequest request, AdminService adminService) {
        this.request = request;
        this.adminService = adminService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @GetMapping("/admin/organization-requests")
    public ResponseEntity<List<OrganizationRoleRequestItem>> adminOrganizationRequestsGet(
            @RequestParam(required = false, defaultValue = "PENDING") String status) {
        try {
            List<OrganizationRoleRequestItem> requests = adminService.getOrganizationRequests(status);
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/admin/organization-requests/{requestId}/approve")
    public ResponseEntity<Void> adminOrganizationRequestsRequestIdApprovePost(
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            adminService.approveOrganizationRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/admin/organization-requests/{requestId}/reject")
    public ResponseEntity<Void> adminOrganizationRequestsRequestIdRejectPost(
            @PathVariable("requestId") @NotNull Integer requestId) {
        try {
            adminService.rejectOrganizationRequest(requestId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/organizations")
    public ResponseEntity<List<Organization>> adminOrganizationsGet() {
        try {
            List<Organization> organizations = adminService.getAllOrganizations();
            return ResponseEntity.ok(organizations);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/admin/organizations")
    public ResponseEntity<Organization> adminOrganizationsPost(@Valid @RequestBody CreateOrganizationRequest createOrganizationRequest) {
        try {
            Organization organization = adminService.createOrganization(createOrganizationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(organization);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/admin/organizations/{id}")
    public ResponseEntity<Organization> adminOrganizationsIdPut(
            @PathVariable("id") @NotNull Integer id,
            @Valid @RequestBody CreateOrganizationRequest createOrganizationRequest) {
        try {
            Organization organization = adminService.updateOrganization(id, createOrganizationRequest);
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/admin/organizations/{id}")
    public ResponseEntity<Void> adminOrganizationsIdDelete(@PathVariable("id") @NotNull Integer id) {
        try {
            adminService.deleteOrganization(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().contains("representatives")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> adminUsersGet() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/admin/users/{userId}/role")
    public ResponseEntity<User> adminUsersUserIdRolePut(
            @PathVariable("userId") @NotNull Integer userId,
            @Valid @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        try {
            User user = adminService.updateUserRole(userId, updateUserRoleRequest);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().contains("required")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/events/individual/{id}")
    public ResponseEntity<Void> eventsIndividualIdDelete(@PathVariable("id") @NotNull Integer id) {
        try {
            adminService.deleteIndividualEvent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators") || 
                e.getMessage().contains("creator") ||
                e.getMessage().contains("Only")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/events/mass/{id}")
    public ResponseEntity<Void> eventsMassIdDelete(@PathVariable("id") @NotNull Integer id) {
        try {
            adminService.deleteMassEvent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (e.getMessage().contains("administrators") || 
                e.getMessage().contains("representative") ||
                e.getMessage().contains("Only")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
