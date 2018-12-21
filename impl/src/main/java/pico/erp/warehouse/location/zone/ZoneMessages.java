package pico.erp.warehouse.location.zone;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.site.Site;

public interface ZoneMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    ZoneId id;

    @Valid
    @NotNull
    ZoneCode code;

    @NotNull
    Site site;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    ZoneCode code;

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
