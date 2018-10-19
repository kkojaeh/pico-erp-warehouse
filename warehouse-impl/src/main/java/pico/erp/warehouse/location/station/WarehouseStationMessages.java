package pico.erp.warehouse.location.station;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.site.WarehouseSite;

public interface WarehouseStationMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseStationId id;

    @Valid
    @NotNull
    WarehouseStationCode code;

    @NotNull
    WarehouseSite site;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseStationCode code;

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

    boolean codeChanged;

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
