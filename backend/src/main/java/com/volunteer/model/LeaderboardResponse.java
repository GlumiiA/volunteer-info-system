package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.volunteer.model.LeaderboardEntry;
import com.volunteer.model.LeaderboardResponseCurrentUserRank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeaderboardResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class LeaderboardResponse {

  @Valid
  private List<@Valid LeaderboardEntry> leaderboard = new ArrayList<>();

  private JsonNullable<LeaderboardResponseCurrentUserRank> currentUserRank = JsonNullable.<LeaderboardResponseCurrentUserRank>undefined();

  public LeaderboardResponse leaderboard(List<@Valid LeaderboardEntry> leaderboard) {
    this.leaderboard = leaderboard;
    return this;
  }

  public LeaderboardResponse addLeaderboardItem(LeaderboardEntry leaderboardItem) {
    if (this.leaderboard == null) {
      this.leaderboard = new ArrayList<>();
    }
    this.leaderboard.add(leaderboardItem);
    return this;
  }

  /**
   * Список лидеров
   * @return leaderboard
   */
  @Valid 
  @Schema(name = "leaderboard", description = "Список лидеров", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaderboard")
  public List<@Valid LeaderboardEntry> getLeaderboard() {
    return leaderboard;
  }

  public void setLeaderboard(List<@Valid LeaderboardEntry> leaderboard) {
    this.leaderboard = leaderboard;
  }

  public LeaderboardResponse currentUserRank(LeaderboardResponseCurrentUserRank currentUserRank) {
    this.currentUserRank = JsonNullable.of(currentUserRank);
    return this;
  }

  /**
   * Get currentUserRank
   * @return currentUserRank
   */
  @Valid 
  @Schema(name = "currentUserRank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentUserRank")
  public JsonNullable<LeaderboardResponseCurrentUserRank> getCurrentUserRank() {
    return currentUserRank;
  }

  public void setCurrentUserRank(JsonNullable<LeaderboardResponseCurrentUserRank> currentUserRank) {
    this.currentUserRank = currentUserRank;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardResponse leaderboardResponse = (LeaderboardResponse) o;
    return Objects.equals(this.leaderboard, leaderboardResponse.leaderboard) &&
        equalsNullable(this.currentUserRank, leaderboardResponse.currentUserRank);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderboard, hashCodeNullable(currentUserRank));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardResponse {\n");
    sb.append("    leaderboard: ").append(toIndentedString(leaderboard)).append("\n");
    sb.append("    currentUserRank: ").append(toIndentedString(currentUserRank)).append("\n");
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

