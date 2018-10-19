package pico.erp.warehouse.location.station;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.site.WarehouseSiteId;

public interface WarehouseStationService {

  WarehouseStationData create(@Valid WarehouseStationRequests.CreateRequest request);

  void delete(@Valid WarehouseStationRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseStationId id);

  WarehouseStationData get(@NotNull WarehouseStationId id);

  List<WarehouseStationData> getAll(@NotNull WarehouseSiteId siteId);

  void update(@Valid WarehouseStationRequests.UpdateRequest request);

}
