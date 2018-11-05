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
import pico.erp.warehouse.location.LocationId;


public interface PackRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    PackId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class PackRequest {

    @Valid
    @NotNull
    PackId id;

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
    PackId id;

    @Valid
    @NotNull
    LocationId locationId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    PackId id;
  }
}
