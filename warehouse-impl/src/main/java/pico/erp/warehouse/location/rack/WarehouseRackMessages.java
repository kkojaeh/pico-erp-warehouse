package pico.erp.warehouse.location.rack;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.zone.WarehouseZone;

public interface WarehouseRackMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseRackId id;

    @Valid
    @NotNull
    WarehouseRackCode code;

    @NotNull
    WarehouseZone zone;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseRackCode code;

  }

  @Data
  class DeleteRequest {

  }

  @Data
  class ResetLocationCodeRequest {

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

  @Value
  class ResetLocationCodeResponse {

    Collection<Event> events;

  }
}
