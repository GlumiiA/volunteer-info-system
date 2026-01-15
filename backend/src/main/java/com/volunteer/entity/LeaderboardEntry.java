package com.volunteer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class LeaderboardEntry {

    @JsonProperty("userId")
    @Schema(description = "User ID")
    private Long userId;

    @JsonProperty("username")
    @NotNull
    @Schema(description = "Username")
    private String username;

    @JsonProperty("score")
    @NotNull
    @Schema(description = "User score")
    private Integer score;

    @JsonProperty("rank")
    @Schema(description = "User rank in leaderboard")
    private Integer rank;

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(Long userId, String username, Integer score, Integer rank) {
        this.userId = userId;
        this.username = username;
        this.score = score;
        this.rank = rank;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }
}
