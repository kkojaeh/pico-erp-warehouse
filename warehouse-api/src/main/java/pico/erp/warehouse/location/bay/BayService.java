package pico.erp.warehouse.location.bay;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.rack.RackId;

public interface BayService {

  BayData create(@Valid BayRequests.CreateRequest request);

  void delete(@Valid BayRequests.DeleteRequest request);

  boolean exists(@NotNull BayId id);

  BayData get(@NotNull BayId id);

  List<BayData> getAll(@NotNull RackId rackId);

  void update(@Valid BayRequests.UpdateRequest request);

}
