package pico.erp.warehouse.transaction;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionRepository {

  WarehouseTransaction create(@NotNull WarehouseTransaction warehouseTransaction);

  void deleteBy(@NotNull WarehouseTransactionId id);

  boolean exists(@NotNull WarehouseTransactionId id);

  Optional<WarehouseTransaction> findBy(@NotNull WarehouseTransactionId id);

  void update(@NotNull WarehouseTransaction warehouseTransaction);

}
