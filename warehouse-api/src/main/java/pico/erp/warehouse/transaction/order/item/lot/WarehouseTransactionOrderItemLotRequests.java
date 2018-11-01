package pico.erp.warehouse.transaction.order.item.lot;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;


public interface WarehouseTransactionOrderItemLotRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderItemLotId id;

    @NotNull
    WarehouseTransactionOrderItemId orderItemId;

    // 대상 회사(회사)
    @NotNull
    ItemLotId itemLotId;

    @Min(0)
    @NotNull
    BigDecimal quantity;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderItemLotId id;

    @Min(0)
    @NotNull
    BigDecimal quantity;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderItemLotId id;
  }


}
