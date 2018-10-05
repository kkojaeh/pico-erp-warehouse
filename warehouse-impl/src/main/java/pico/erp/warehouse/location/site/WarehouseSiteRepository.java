package pico.erp.warehouse.location.site;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.WarehouseLocationCode;

public interface WarehouseSiteRepository {

  WarehouseSite create(@NotNull WarehouseSite warehouseSite);

  void deleteBy(@NotNull WarehouseSiteId id);

  boolean exists(@NotNull WarehouseSiteId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Optional<WarehouseSite> findBy(@NotNull WarehouseSiteId id);

  void update(@NotNull WarehouseSite warehouseSite);

}
