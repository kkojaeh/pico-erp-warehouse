package pico.erp.warehouse.location.bay;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.rack.WarehouseRackId;

public interface WarehouseBayService {

  WarehouseBayData create(@Valid WarehouseBayRequests.CreateRequest request);

  void delete(@Valid WarehouseBayRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseBayId id);

  WarehouseBayData get(@NotNull WarehouseBayId id);

  List<WarehouseBayData> getAll(@NotNull WarehouseRackId rackId);

  void update(@Valid WarehouseBayRequests.UpdateRequest request);

}
