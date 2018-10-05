package pico.erp.warehouse.location.zone;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.site.WarehouseSiteId;

public interface WarehouseZoneService {

  WarehouseZoneData create(@Valid WarehouseZoneRequests.CreateRequest request);

  void delete(@Valid WarehouseZoneRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseZoneId id);

  WarehouseZoneData get(@NotNull WarehouseZoneId id);

  List<WarehouseZoneData> getAll(@NotNull WarehouseSiteId siteId);

  void update(@Valid WarehouseZoneRequests.UpdateRequest request);

}
