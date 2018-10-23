package pico.erp.warehouse.location.station;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;
import pico.erp.warehouse.location.site.WarehouseSiteId;


public interface WarehouseStationRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseStationId id;

    @Valid
    @NotNull
    WarehouseStationCode code;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Valid
    @NotNull
    WarehouseSiteId siteId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseStationId id;

    @Valid
    @NotNull
    WarehouseStationCode code;

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
    WarehouseStationId id;
  }
}
