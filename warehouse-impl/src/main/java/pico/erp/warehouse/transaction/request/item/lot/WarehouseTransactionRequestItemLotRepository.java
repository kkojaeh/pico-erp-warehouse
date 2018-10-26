package pico.erp.warehouse.transaction.request.item.lot;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;

public interface WarehouseTransactionRequestItemLotRepository {

  WarehouseTransactionRequestItemLot create(
    @NotNull WarehouseTransactionRequestItemLot warehouseTransactionRequestItemLot);

  void deleteBy(@NotNull WarehouseTransactionRequestItemLotId id);

  boolean exists(@NotNull WarehouseTransactionRequestItemLotId id);

  boolean exists(@NotNull WarehouseTransactionRequestItemId transactionRequestItemId,
    @NotNull ItemLotId itemLotId);

  Stream<WarehouseTransactionRequestItemLot> findAllBy(
    @NotNull WarehouseTransactionRequestItemId transactionRequestItemId);

  Stream<WarehouseTransactionRequestItemLot> findAllBy(
    @NotNull WarehouseTransactionRequestId transactionRequestId);

  Optional<WarehouseTransactionRequestItemLot> findBy(
    @NotNull WarehouseTransactionRequestItemLotId id);

  void update(@NotNull WarehouseTransactionRequestItemLot warehouseTransactionRequestItemLot);

}
