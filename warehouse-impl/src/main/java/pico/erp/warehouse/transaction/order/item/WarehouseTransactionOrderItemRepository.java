package pico.erp.warehouse.transaction.order.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

public interface WarehouseTransactionOrderItemRepository {

  WarehouseTransactionOrderItem create(
    @NotNull WarehouseTransactionOrderItem warehouseTransactionRequestItem);

  void deleteBy(@NotNull WarehouseTransactionOrderItemId id);

  boolean exists(@NotNull WarehouseTransactionOrderItemId id);

  boolean exists(@NotNull WarehouseTransactionOrderId transactionOrderId,
    @NotNull ItemId itemId);

  Stream<WarehouseTransactionOrderItem> findAllBy(
    @NotNull WarehouseTransactionOrderId transactionOrderId);

  Optional<WarehouseTransactionOrderItem> findBy(@NotNull WarehouseTransactionOrderItemId id);

  void update(@NotNull WarehouseTransactionOrderItem warehouseTransactionRequestItem);

}
