package pico.erp.warehouse.location.zone;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.site.WarehouseSiteId;

public interface WarehouseZoneRepository {

  WarehouseZone create(@NotNull WarehouseZone warehouseZone);

  void deleteBy(@NotNull WarehouseZoneId id);

  boolean exists(@NotNull WarehouseZoneId id);

  Stream<WarehouseZone> findAllBy(@NotNull WarehouseSiteId siteId);

  Optional<WarehouseZone> findBy(@NotNull WarehouseZoneId id);

  void update(@NotNull WarehouseZone warehouseZone);

}
