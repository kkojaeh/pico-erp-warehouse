package pico.erp.warehouse.location.bay;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.rack.WarehouseRackId;

public interface WarehouseBayRepository {

  WarehouseBay create(@NotNull WarehouseBay warehouseBay);

  void deleteBy(@NotNull WarehouseBayId id);

  boolean exists(@NotNull WarehouseBayId id);

  boolean exists(@NotNull WarehouseLocationCode locationCode);

  Stream<WarehouseBay> findAllBy(@NotNull WarehouseRackId rackId);

  Optional<WarehouseBay> findBy(@NotNull WarehouseBayId id);

  void update(@NotNull WarehouseBay warehouseBay);

}
