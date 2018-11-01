package pico.erp.warehouse.transaction.order.pack;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.pack.WarehousePack;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrder;

public interface WarehouseTransactionOrderPackMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderPackId id;

    @NotNull
    WarehouseTransactionOrder order;

    @NotNull
    WarehousePack pack;

  }

  @Builder
  @Data
  class UpdateRequest {

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
