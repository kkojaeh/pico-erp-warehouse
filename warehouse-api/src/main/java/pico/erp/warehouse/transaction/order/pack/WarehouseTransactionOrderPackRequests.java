package pico.erp.warehouse.transaction.order.pack;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.pack.WarehousePackId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;


public interface WarehouseTransactionOrderPackRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderPackId id;

    @NotNull
    WarehouseTransactionOrderId orderId;

    @NotNull
    WarehousePackId packId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderPackId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderPackId id;
  }


}
