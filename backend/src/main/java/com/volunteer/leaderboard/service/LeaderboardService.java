package com.volunteer.leaderboard.service;

import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.service.CurrentUserService;
import com.volunteer.entity.User;
import com.volunteer.leaderboard.dto.LeaderboardEntry;
import com.volunteer.leaderboard.dto.LeaderboardResponse;
import com.volunteer.leaderboard.dto.LeaderboardResponseCurrentUserRank;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public LeaderboardService(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public LeaderboardResponse getLeaderboard(Integer limit) {
        // Get all users sorted by volunteerHours descending
        List<User> users = userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getVolunteerHours, 
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit != null ? limit : 100)
                .collect(Collectors.toList());

        // Convert to LeaderboardEntry with ranks
        List<LeaderboardEntry> entries = users.stream()
                .map(user -> {
                    LeaderboardEntry entry = new LeaderboardEntry();
                    entry.setUserId(user.getId());
                    entry.setFullName(user.getName());
                    entry.setScore(user.getVolunteerHours() != null 
                            ? user.getVolunteerHours().intValue() : 0);
                    return entry;
                })
                .collect(Collectors.toList());

        // Assign ranks (1-based)
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }

        LeaderboardResponse response = new LeaderboardResponse();
        response.setLeaderboard(entries);

        // Get current user rank if authenticated
        try {
            Optional<User> currentUserOpt = Optional.ofNullable(currentUserService.getCurrentUser());
            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                
                // Calculate rank by finding position in sorted list
                List<User> allUsers = userRepository.findAll().stream()
                        .sorted(Comparator.comparing(User::getVolunteerHours, 
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
                
                int rank = 1;
                Integer userScore = currentUser.getVolunteerHours() != null 
                        ? currentUser.getVolunteerHours().intValue() : 0;
                
                for (User user : allUsers) {
                    Integer userVolunteerHours = user.getVolunteerHours() != null 
                            ? user.getVolunteerHours().intValue() : 0;
                    if (user.getId().equals(currentUser.getId())) {
                        LeaderboardResponseCurrentUserRank currentUserRank = 
                                new LeaderboardResponseCurrentUserRank();
                        currentUserRank.setRank(rank);
                        currentUserRank.setScore(userScore);
                        response.setCurrentUserRank(JsonNullable.of(currentUserRank));
                        break;
                    }
                    rank++;
                }
            }
        } catch (Exception e) {
            // User not authenticated, leave currentUserRank as null
        }

        return response;
    }
}
