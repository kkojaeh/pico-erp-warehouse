package pico.erp.warehouse.location.station;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.warehouse.location.site.SiteId;


public interface StationRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    StationId id;

    @Valid
    @NotNull
    StationCode code;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Valid
    @NotNull
    SiteId siteId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    StationId id;

    @Valid
    @NotNull
    StationCode code;

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
    StationId id;
  }
}
