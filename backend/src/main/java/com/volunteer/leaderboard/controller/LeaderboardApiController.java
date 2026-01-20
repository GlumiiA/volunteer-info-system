package com.volunteer.leaderboard.controller;

import com.volunteer.leaderboard.dto.LeaderboardResponse;
import com.volunteer.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;

@Controller
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class LeaderboardApiController {

    private final NativeWebRequest request;
    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardApiController(NativeWebRequest request, LeaderboardService leaderboardService) {
        this.request = request;
        this.leaderboardService = leaderboardService;
    }

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardResponse> leaderboardGet(
            @RequestParam(required = false, defaultValue = "100") @Min(1) @Max(500) Integer limit) {
        try {
            LeaderboardResponse response = leaderboardService.getLeaderboard(limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
