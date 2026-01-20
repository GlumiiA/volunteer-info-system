package com.volunteer.user.controller;

import com.volunteer.entity.Review;
import com.volunteer.entity.User;
import com.volunteer.organization.dto.OrganizationRoleRequest;
import com.volunteer.user.dto.UpdateUserRequest;
import com.volunteer.user.dto.UsersMeAvatar;
import com.volunteer.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
@RestController
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class UsersApiController implements UsersApi {

    private final NativeWebRequest request;
    private final UserService userService;

    @Autowired
    public UsersApiController(NativeWebRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @GetMapping("/users/me")
    public ResponseEntity<User> usersMeGet() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/me")
    public ResponseEntity<User> usersMePut(@RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateCurrentUser(updateUserRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/users/me/avatar", consumes = "multipart/form-data")
    public ResponseEntity<UsersMeAvatar> usersMeAvatarPost(@RequestPart("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        UsersMeAvatar response = new UsersMeAvatar();
        response.setAvatarUrl(avatarUrl);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/me/avatar")
    public ResponseEntity<Void> usersMeAvatarDelete() {
        userService.deleteAvatar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/events")
    public ResponseEntity<com.volunteer.user.dto.UsersMeEvents> usersMeEventsGet(
            @RequestParam(required = false, defaultValue = "all") String status) {
        com.volunteer.user.dto.UsersMeEvents events = userService.getUserEvents(status);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/users/me/reviews")
    public ResponseEntity<List<Review>> usersMeReviewsGet() {
        List<Review> reviews = userService.getUserReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> usersIdGet(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/me/volunteer-book")
    public ResponseEntity<com.volunteer.user.dto.UsersMeVolunteerBook> usersMeVolunteerBookGet() {
        com.volunteer.user.dto.UsersMeVolunteerBook book = userService.getUserVolunteerBook();
        return ResponseEntity.ok(book);
    }

    @PostMapping("/users/me/organization-request")
    public ResponseEntity<Void> usersMeOrganizationRequestPost(@RequestBody OrganizationRoleRequest request) {
        try {
            userService.createOrganizationRoleRequest(request);
            return ResponseEntity.status(201).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already a representative")) {
                return ResponseEntity.status(400).build();
            }
            if (e.getMessage().contains("already has a pending")) {
                return ResponseEntity.status(400).build();
            }
            if (e.getMessage().contains("required") || e.getMessage().contains("not found")) {
                return ResponseEntity.status(400).build();
            }
            throw e;
        }
    }
}
