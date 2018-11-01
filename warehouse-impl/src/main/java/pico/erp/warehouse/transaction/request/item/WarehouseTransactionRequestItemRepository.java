package pico.erp.warehouse.transaction.request.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;

public interface WarehouseTransactionRequestItemRepository {

  WarehouseTransactionRequestItem create(
    @NotNull WarehouseTransactionRequestItem warehouseTransactionRequestItem);

  void deleteBy(@NotNull WarehouseTransactionRequestItemId id);

  boolean exists(@NotNull WarehouseTransactionRequestItemId id);

  boolean exists(@NotNull WarehouseTransactionRequestId requestId,
    @NotNull ItemId itemId);

  Stream<WarehouseTransactionRequestItem> findAllBy(
    @NotNull WarehouseTransactionRequestId requestId);

  Optional<WarehouseTransactionRequestItem> findBy(@NotNull WarehouseTransactionRequestItemId id);

  void update(@NotNull WarehouseTransactionRequestItem warehouseTransactionRequestItem);

}
