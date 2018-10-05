package pico.erp.warehouse.location.level;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.location.bay.WarehouseBayId;


public interface WarehouseLevelRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseLevelId id;

    @Valid
    @NotNull
    WarehouseLevelCode code;

    @Valid
    @NotNull
    WarehouseBayId bayId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseLevelId id;

    @Valid
    @NotNull
    WarehouseLevelCode code;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseLevelId id;
  }
}
