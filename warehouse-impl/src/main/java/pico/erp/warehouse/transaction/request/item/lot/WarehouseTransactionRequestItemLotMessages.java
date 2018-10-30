package pico.erp.warehouse.transaction.request.item.lot;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItem;

public interface WarehouseTransactionRequestItemLotMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestItemLotId id;

    @NotNull
    WarehouseTransactionRequestItem transactionRequestItem;

    @NotNull
    ItemLotData itemLot;

    @Min(0)
    @NotNull
    BigDecimal quantity;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class UpdateRequest {

    @Min(0)
    @NotNull
    BigDecimal quantity;

  }

  @Builder
  @Data
  class DeleteRequest {

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

}
