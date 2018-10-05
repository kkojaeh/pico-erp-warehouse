package pico.erp.warehouse.location.bay;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.rack.WarehouseRack;

public interface WarehouseBayMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseBayId id;

    @Valid
    @NotNull
    WarehouseBayCode code;

    @NotNull
    WarehouseRack rack;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseBayCode code;

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
