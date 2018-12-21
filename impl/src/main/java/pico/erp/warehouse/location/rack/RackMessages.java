package pico.erp.warehouse.location.rack;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.zone.Zone;

public interface RackMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    RackId id;

    @Valid
    @NotNull
    RackCode code;

    @NotNull
    Zone zone;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    RackCode code;

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
