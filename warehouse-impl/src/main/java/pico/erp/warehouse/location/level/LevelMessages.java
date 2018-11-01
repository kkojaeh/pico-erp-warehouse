package pico.erp.warehouse.location.level;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.bay.Bay;

public interface LevelMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    LevelId id;

    @Valid
    @NotNull
    LevelCode code;

    @NotNull
    Bay bay;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    LevelCode code;

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
