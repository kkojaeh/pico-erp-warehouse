package pico.erp.warehouse.location.level;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.bay.WarehouseBayId;

public interface WarehouseLevelRepository {

  WarehouseLevel create(@NotNull WarehouseLevel warehouseLevel);

  void deleteBy(@NotNull WarehouseLevelId id);

  boolean exists(@NotNull WarehouseLevelId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Stream<WarehouseLevel> findAllBy(@NotNull WarehouseBayId bayId);

  Optional<WarehouseLevel> findBy(@NotNull WarehouseLevelId id);

  void update(@NotNull WarehouseLevel warehouseLevel);

}
