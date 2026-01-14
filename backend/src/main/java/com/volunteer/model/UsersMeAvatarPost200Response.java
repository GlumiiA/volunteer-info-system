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
 * UsersMeAvatarPost200Response
 */

@JsonTypeName("_users_me_avatar_post_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T10:36:20.114917+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class UsersMeAvatarPost200Response {

  private @Nullable String avatarUrl;

  public UsersMeAvatarPost200Response avatarUrl(@Nullable String avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  /**
   * URL загруженного аватара
   * @return avatarUrl
   */
  
  @Schema(name = "avatarUrl", description = "URL загруженного аватара", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avatarUrl")
  public @Nullable String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(@Nullable String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersMeAvatarPost200Response usersMeAvatarPost200Response = (UsersMeAvatarPost200Response) o;
    return Objects.equals(this.avatarUrl, usersMeAvatarPost200Response.avatarUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(avatarUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersMeAvatarPost200Response {\n");
    sb.append("    avatarUrl: ").append(toIndentedString(avatarUrl)).append("\n");
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

