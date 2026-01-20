package com.volunteer.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class LeaderboardEntry {

    @JsonProperty("userId")
    @Schema(description = "ID пользователя")
    private Integer userId;

    @JsonProperty("fullName")
    @NotNull
    @Schema(description = "Полное имя пользователя")
    private String fullName;

    @JsonProperty("score")
    @NotNull
    @Schema(description = "User score")
    private Integer score;

    @JsonProperty("rank")
    @Schema(description = "User rank in leaderboard")
    private Integer rank;

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(Integer userId, String fullName, Integer score, Integer rank) {
        this.userId = userId;
        this.fullName = fullName;
        this.score = score;
        this.rank = rank;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        return userId != null && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }
}
