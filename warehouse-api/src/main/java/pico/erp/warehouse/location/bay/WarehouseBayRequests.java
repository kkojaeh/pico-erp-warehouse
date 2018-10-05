package pico.erp.warehouse.location.bay;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.location.rack.WarehouseRackId;


public interface WarehouseBayRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseBayId id;

    @Valid
    @NotNull
    WarehouseBayCode code;

    @Valid
    @NotNull
    WarehouseRackId rackId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseBayId id;

    @Valid
    @NotNull
    WarehouseBayCode code;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseBayId id;
  }
}
