package pico.erp.warehouse.location.site;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface WarehouseSiteMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseSiteId id;

    @Valid
    @NotNull
    WarehouseSiteCode code;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseSiteCode code;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

  }

  @Data
  class DeleteRequest {

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
}
