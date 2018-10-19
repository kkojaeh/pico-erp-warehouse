package pico.erp.warehouse.pack;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface WarehousePackRepository {

  long countByCreatedToday();

  WarehousePack create(@NotNull WarehousePack warehousePack);

  void deleteBy(@NotNull WarehousePackId id);

  boolean exists(@NotNull WarehousePackCode packCode);

  boolean exists(@NotNull WarehousePackId id);

  Optional<WarehousePack> findBy(@NotNull WarehousePackId id);

  void update(@NotNull WarehousePack warehousePack);

}
