package com.volunteer.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeaderboardEntry
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class LeaderboardEntry {

  private @Nullable Integer rank;

  private @Nullable Integer userId;

  private @Nullable String fullName;

  private @Nullable Integer score;

  public LeaderboardEntry rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Место в рейтинге
   * @return rank
   */
  
  @Schema(name = "rank", description = "Место в рейтинге", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public LeaderboardEntry userId(@Nullable Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * ID пользователя
   * @return userId
   */
  
  @Schema(name = "userId", description = "ID пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public @Nullable Integer getUserId() {
    return userId;
  }

  public void setUserId(@Nullable Integer userId) {
    this.userId = userId;
  }

  public LeaderboardEntry fullName(@Nullable String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Полное имя пользователя
   * @return fullName
   */
  
  @Schema(name = "fullName", description = "Полное имя пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fullName")
  public @Nullable String getFullName() {
    return fullName;
  }

  public void setFullName(@Nullable String fullName) {
    this.fullName = fullName;
  }

  public LeaderboardEntry score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Количество баллов/часов
   * @return score
   */
  
  @Schema(name = "score", description = "Количество баллов/часов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable Integer getScore() {
    return score;
  }

  public void setScore(@Nullable Integer score) {
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardEntry leaderboardEntry = (LeaderboardEntry) o;
    return Objects.equals(this.rank, leaderboardEntry.rank) &&
        Objects.equals(this.userId, leaderboardEntry.userId) &&
        Objects.equals(this.fullName, leaderboardEntry.fullName) &&
        Objects.equals(this.score, leaderboardEntry.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, userId, fullName, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntry {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

