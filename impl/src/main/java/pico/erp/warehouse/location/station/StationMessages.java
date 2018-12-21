package pico.erp.warehouse.location.station;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.site.Site;

public interface StationMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    StationId id;

    @Valid
    @NotNull
    StationCode code;

    @NotNull
    Site site;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    StationCode code;

    @NotNull
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

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
