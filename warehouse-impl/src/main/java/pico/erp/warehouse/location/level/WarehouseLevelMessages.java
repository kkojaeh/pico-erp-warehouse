package pico.erp.warehouse.location.level;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.bay.WarehouseBay;

public interface WarehouseLevelMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseLevelId id;

    @Valid
    @NotNull
    WarehouseLevelCode code;

    @NotNull
    WarehouseBay bay;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseLevelCode code;

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
