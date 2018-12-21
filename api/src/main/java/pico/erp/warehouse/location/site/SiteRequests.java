package pico.erp.warehouse.location.site;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;


public interface SiteRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    SiteId id;

    @Valid
    @NotNull
    SiteCode code;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    SiteId id;

    @Valid
    @NotNull
    SiteCode code;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    SiteId id;
  }
}
