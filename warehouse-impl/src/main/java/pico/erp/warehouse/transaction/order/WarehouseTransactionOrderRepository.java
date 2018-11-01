package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionOrderRepository {

  WarehouseTransactionOrder create(
    @NotNull WarehouseTransactionOrder warehouseTransactionOrder);

  void deleteBy(@NotNull WarehouseTransactionOrderId id);

  boolean exists(@NotNull WarehouseTransactionOrderId id);

  Optional<WarehouseTransactionOrderAggregator> findAggregatorBy(
    @NotNull WarehouseTransactionOrderId id);

  Stream<WarehouseTransactionOrder> findAllUncommittedAt(@NotNull OffsetDateTime fixedDate);

  Optional<WarehouseTransactionOrder> findBy(@NotNull WarehouseTransactionOrderId id);

  void update(@NotNull WarehouseTransactionOrder warehouseTransactionOrder);

}
