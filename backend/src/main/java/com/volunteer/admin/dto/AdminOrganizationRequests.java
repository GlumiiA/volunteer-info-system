package com.volunteer.admin.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * AdminOrganizationRequestsRequestIdRejectPostRequest
 */

@JsonTypeName("_admin_organization_requests__requestId__reject_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
public class AdminOrganizationRequests {

  private @Nullable String reason;

  public AdminOrganizationRequests reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина отклонения запроса
   * @return reason
   */
  
  @Schema(name = "reason", description = "Причина отклонения запроса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminOrganizationRequests adminOrganizationRequestsRequestIdRejectPostRequest = (AdminOrganizationRequests) o;
    return Objects.equals(this.reason, adminOrganizationRequestsRequestIdRejectPostRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminOrganizationRequestsRequestIdRejectPostRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

