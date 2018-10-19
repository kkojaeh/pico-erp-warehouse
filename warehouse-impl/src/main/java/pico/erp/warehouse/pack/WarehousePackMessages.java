package pico.erp.warehouse.pack;

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
import pico.erp.warehouse.location.WarehouseLocation;
import pico.erp.warehouse.pack.code.WarehousePackCodeGenerator;

public interface WarehousePackMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehousePackId id;

    @NotNull
    WarehousePackCodeGenerator codeGenerator;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class PackRequest {

    @Valid
    @NotNull
    ItemLotData itemLot;

    @Min(1)
    @NotNull
    BigDecimal quantity;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class PutRequest {

    WarehouseLocation location;

  }

  @Data
  class DeleteRequest {

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class PackResponse {

    Collection<Event> events;

  }

  @Value
  class PutResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

}
