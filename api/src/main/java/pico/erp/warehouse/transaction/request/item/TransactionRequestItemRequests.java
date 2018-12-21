package pico.erp.warehouse.transaction.request.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.TransactionRequestId;


public interface TransactionRequestItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionRequestItemId id;

    @NotNull
    TransactionRequestId requestId;

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
    TransactionRequestItemId id;

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
    TransactionRequestItemId id;
  }


}
