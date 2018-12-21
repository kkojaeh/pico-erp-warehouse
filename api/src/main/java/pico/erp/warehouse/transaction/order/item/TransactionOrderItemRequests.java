package pico.erp.warehouse.transaction.order.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;


public interface TransactionOrderItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderItemId id;

    @NotNull
    TransactionOrderId orderId;

    // 대상 회사(회사)
    @NotNull
    ItemId itemId;

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
    TransactionOrderItemId id;

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
    TransactionOrderItemId id;
  }


}
