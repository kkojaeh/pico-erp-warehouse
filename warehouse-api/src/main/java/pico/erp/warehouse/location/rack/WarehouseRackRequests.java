package pico.erp.warehouse.location.rack;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.location.zone.WarehouseZoneId;


public interface WarehouseRackRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseRackId id;

    @Valid
    @NotNull
    WarehouseRackCode code;

    @Valid
    @NotNull
    WarehouseZoneId zoneId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseRackId id;

    @Valid
    @NotNull
    WarehouseRackCode code;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseRackId id;
  }
}
