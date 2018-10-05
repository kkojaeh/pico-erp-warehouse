package pico.erp.warehouse.location.site;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehouseSiteService {

  WarehouseSiteData create(@Valid WarehouseSiteRequests.CreateRequest request);

  void delete(@Valid WarehouseSiteRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseSiteId id);

  WarehouseSiteData get(@NotNull WarehouseSiteId id);

  void update(@Valid WarehouseSiteRequests.UpdateRequest request);

}
