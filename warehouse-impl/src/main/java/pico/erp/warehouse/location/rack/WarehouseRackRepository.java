package pico.erp.warehouse.location.rack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.zone.WarehouseZoneId;

public interface WarehouseRackRepository {

  WarehouseRack create(@NotNull WarehouseRack warehouseRack);

  void deleteBy(@NotNull WarehouseRackId id);

  boolean exists(@NotNull WarehouseRackId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Stream<WarehouseRack> findAllBy(@NotNull WarehouseZoneId zoneId);

  Optional<WarehouseRack> findBy(@NotNull WarehouseRackId id);

  void update(@NotNull WarehouseRack warehouseRack);

}
