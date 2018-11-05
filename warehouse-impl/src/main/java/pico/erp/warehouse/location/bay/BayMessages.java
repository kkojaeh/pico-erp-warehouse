package pico.erp.warehouse.location.bay;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.rack.Rack;

public interface BayMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    BayId id;

    @Valid
    @NotNull
    BayCode code;

    @NotNull
    Rack rack;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    BayCode code;

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
