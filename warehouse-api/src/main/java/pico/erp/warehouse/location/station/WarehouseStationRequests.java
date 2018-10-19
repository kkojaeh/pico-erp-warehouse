package pico.erp.warehouse.location.station;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
