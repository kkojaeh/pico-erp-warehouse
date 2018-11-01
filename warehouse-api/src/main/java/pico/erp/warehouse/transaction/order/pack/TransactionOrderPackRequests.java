package pico.erp.warehouse.transaction.order.pack;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.warehouse.pack.PackId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;


public interface TransactionOrderPackRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderPackId id;

    @NotNull
    TransactionOrderId orderId;

    @NotNull
    PackId packId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    TransactionOrderPackId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    TransactionOrderPackId id;
  }


}
