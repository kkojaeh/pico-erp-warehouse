package pico.erp.warehouse.location;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.shared.event.Event;

public interface WarehouseLocationMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseLocationId id;

    @Valid
    @NotNull
    WarehouseLocationCode code;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseLocationCode code;

  }

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

    boolean codeChanged;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

}
