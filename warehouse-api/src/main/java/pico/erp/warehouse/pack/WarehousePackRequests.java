package pico.erp.warehouse.pack;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.location.WarehouseLocationId;


public interface WarehousePackRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehousePackId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class PackRequest {

    @Valid
    @NotNull
    WarehousePackId id;

    @Valid
    @NotNull
    ItemLotId itemLotId;

    @Min(1)
    @NotNull
    BigDecimal quantity;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class PutRequest {

    @Valid
    @NotNull
    WarehousePackId id;

    @Valid
    @NotNull
    WarehouseLocationId locationId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehousePackId id;
  }
}
