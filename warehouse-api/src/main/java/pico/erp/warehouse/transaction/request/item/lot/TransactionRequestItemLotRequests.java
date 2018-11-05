package pico.erp.warehouse.transaction.request.item.lot;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;


public interface TransactionRequestItemLotRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionRequestItemLotId id;

    @NotNull
    TransactionRequestItemId requestItemId;

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
    TransactionRequestItemLotId id;

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
    TransactionRequestItemLotId id;
  }


}
