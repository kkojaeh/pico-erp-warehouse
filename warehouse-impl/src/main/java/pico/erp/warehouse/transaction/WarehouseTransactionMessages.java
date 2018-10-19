package pico.erp.warehouse.transaction;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;

public interface WarehouseTransactionMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class InboundRequest {

    @Valid
    @NotNull
    ItemLotData itemLot;

    @Valid
    @NotNull
    BigDecimal quantity;

    @NotNull
    Auditor transactedBy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class OutboundRequest {

    @Valid
    @NotNull
    ItemLotData itemLot;

    @Valid
    @NotNull
    BigDecimal quantity;

    @NotNull
    Auditor transactedBy;

  }

  @Value
  class InboundResponse {

    Collection<Event> events;

  }

  @Value
  class OutboundResponse {

    Collection<Event> events;

  }

}
