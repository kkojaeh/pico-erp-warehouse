package pico.erp.warehouse.location.zone;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.site.WarehouseSite;

public interface WarehouseZoneMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseZoneId id;

    @Valid
    @NotNull
    WarehouseZoneCode code;

    @NotNull
    WarehouseSite site;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseZoneCode code;

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
