package pico.erp.warehouse.transaction;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.lot.ItemLotId;


public interface WarehouseTransactionRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InboundRequest {

    @Valid
    @NotNull
    ItemLotId itemLotId;

    @Valid
    @NotNull
    BigDecimal quantity;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class OutboundRequest {

    @Valid
    @NotNull
    ItemLotId itemLotId;

    @Valid
    @NotNull
    BigDecimal quantity;

  }

}
