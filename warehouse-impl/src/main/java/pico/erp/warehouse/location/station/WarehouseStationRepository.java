package pico.erp.warehouse.location.station;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.site.WarehouseSiteId;

public interface WarehouseStationRepository {

  WarehouseStation create(@NotNull WarehouseStation warehouseStation);

  void deleteBy(@NotNull WarehouseStationId id);

  boolean exists(@NotNull WarehouseStationId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Stream<WarehouseStation> findAllBy(@NotNull WarehouseSiteId siteId);

  Optional<WarehouseStation> findBy(@NotNull WarehouseStationId id);

  void update(@NotNull WarehouseStation warehouseStation);

}
