package pico.erp.warehouse.location;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface WarehouseLocationRepository {

  WarehouseLocation create(@NotNull WarehouseLocation warehouseLocation);

  void deleteBy(@NotNull WarehouseLocationId id);

  boolean exists(@NotNull WarehouseLocationId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Optional<WarehouseLocation> findBy(@NotNull WarehouseLocationId id);

  void update(@NotNull WarehouseLocation warehouseLocation);

}
