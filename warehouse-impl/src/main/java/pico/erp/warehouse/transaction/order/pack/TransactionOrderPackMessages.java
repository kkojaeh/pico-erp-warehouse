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
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.transaction.order.TransactionOrder;

public interface TransactionOrderPackMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderPackId id;

    @NotNull
    TransactionOrder order;

    @NotNull
    Pack pack;

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
