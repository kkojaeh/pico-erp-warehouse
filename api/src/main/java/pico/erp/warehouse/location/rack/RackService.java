package pico.erp.warehouse.location.rack;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.zone.ZoneId;

public interface RackService {

  RackData create(@Valid RackRequests.CreateRequest request);

  void delete(@Valid RackRequests.DeleteRequest request);

  boolean exists(@NotNull RackId id);

  RackData get(@NotNull RackId id);

  List<RackData> getAll(@NotNull ZoneId zoneId);

  void update(@Valid RackRequests.UpdateRequest request);

}
