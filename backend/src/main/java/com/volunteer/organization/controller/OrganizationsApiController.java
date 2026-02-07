package com.volunteer.organization.controller;

import com.volunteer.entity.Organization;
import com.volunteer.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class OrganizationsApiController implements OrganizationsApi {

    private final NativeWebRequest request;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationsApiController(NativeWebRequest request, OrganizationRepository organizationRepository) {
        this.request = request;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> organizationsGet() {
        try {
            List<Organization> organizations = organizationRepository.findAll();
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            // Return empty list on error to match API signature
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
