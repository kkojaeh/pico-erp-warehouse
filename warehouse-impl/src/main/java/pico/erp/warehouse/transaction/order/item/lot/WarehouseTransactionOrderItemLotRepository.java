package pico.erp.warehouse.transaction.order.item.lot;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;

public interface WarehouseTransactionOrderItemLotRepository {

  WarehouseTransactionOrderItemLot create(
    @NotNull WarehouseTransactionOrderItemLot orderItemLot);

  void deleteBy(@NotNull WarehouseTransactionOrderItemLotId id);

  boolean exists(@NotNull WarehouseTransactionOrderItemLotId id);

  boolean exists(@NotNull WarehouseTransactionOrderItemId orderItemId,
    @NotNull ItemLotId itemLotId);

  Stream<WarehouseTransactionOrderItemLot> findAllBy(
    @NotNull WarehouseTransactionOrderItemId orderItemId);

  Stream<WarehouseTransactionOrderItemLot> findAllBy(
    @NotNull WarehouseTransactionOrderId orderId);

  Optional<WarehouseTransactionOrderItemLot> findBy(
    @NotNull WarehouseTransactionOrderItemLotId id);

  void update(@NotNull WarehouseTransactionOrderItemLot orderItemLot);

}
