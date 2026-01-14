package com.volunteer.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Позиция текущего пользователя (если авторизован)
 */

@Schema(name = "LeaderboardResponse_currentUserRank", description = "Позиция текущего пользователя (если авторизован)")
@JsonTypeName("LeaderboardResponse_currentUserRank")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class LeaderboardResponseCurrentUserRank {

  private @Nullable Integer rank;

  private @Nullable Integer score;

  public LeaderboardResponseCurrentUserRank rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public LeaderboardResponseCurrentUserRank score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    LeaderboardResponseCurrentUserRank leaderboardResponseCurrentUserRank = (LeaderboardResponseCurrentUserRank) o;
    return Objects.equals(this.rank, leaderboardResponseCurrentUserRank.rank) &&
        Objects.equals(this.score, leaderboardResponseCurrentUserRank.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardResponseCurrentUserRank {\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
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

