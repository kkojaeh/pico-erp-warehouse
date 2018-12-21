package pico.erp.warehouse.transaction.order.item;

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
import pico.erp.item.ItemData;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.transaction.order.TransactionOrder;

public interface TransactionOrderItemMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderItemId id;

    @NotNull
    TransactionOrder order;

    @NotNull
    ItemData item;

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
