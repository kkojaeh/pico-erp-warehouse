package pico.erp.warehouse.location.rack;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.zone.WarehouseZoneId;

public interface WarehouseRackService {

  WarehouseRackData create(@Valid WarehouseRackRequests.CreateRequest request);

  void delete(@Valid WarehouseRackRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseRackId id);

  WarehouseRackData get(@NotNull WarehouseRackId id);

  List<WarehouseRackData> getAll(@NotNull WarehouseZoneId zoneId);

  void update(@Valid WarehouseRackRequests.UpdateRequest request);

}
