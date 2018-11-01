package pico.erp.warehouse.transaction.request.item.lot;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;

public interface WarehouseTransactionRequestItemLotRepository {

  WarehouseTransactionRequestItemLot create(
    @NotNull WarehouseTransactionRequestItemLot requestItemLot);

  void deleteBy(@NotNull WarehouseTransactionRequestItemLotId id);

  boolean exists(@NotNull WarehouseTransactionRequestItemLotId id);

  boolean exists(@NotNull WarehouseTransactionRequestItemId requestItemId,
    @NotNull ItemLotId itemLotId);

  Stream<WarehouseTransactionRequestItemLot> findAllBy(
    @NotNull WarehouseTransactionRequestItemId requestItemId);

  Stream<WarehouseTransactionRequestItemLot> findAllBy(
    @NotNull WarehouseTransactionRequestId requestId);

  Optional<WarehouseTransactionRequestItemLot> findBy(
    @NotNull WarehouseTransactionRequestItemLotId id);

  void update(@NotNull WarehouseTransactionRequestItemLot requestItemLot);

}
