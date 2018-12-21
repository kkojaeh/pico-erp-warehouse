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
import pico.erp.warehouse.location.Location;

public interface PackMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    PackId id;

    @NotNull
    PackCodeGenerator codeGenerator;

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

    Location location;

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
