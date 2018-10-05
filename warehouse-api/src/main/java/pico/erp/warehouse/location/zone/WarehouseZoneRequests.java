package pico.erp.warehouse.location.zone;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.location.site.WarehouseSiteId;


public interface WarehouseZoneRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseZoneId id;

    @Valid
    @NotNull
    WarehouseZoneCode code;

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
    WarehouseZoneId id;

    @Valid
    @NotNull
    WarehouseZoneCode code;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseZoneId id;
  }
}
