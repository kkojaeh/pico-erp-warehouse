package pico.erp.warehouse.transaction.order.pack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

public interface WarehouseTransactionOrderPackRepository {

  WarehouseTransactionOrderPack create(
    @NotNull WarehouseTransactionOrderPack orderPack);

  void deleteBy(@NotNull WarehouseTransactionOrderPackId id);

  boolean exists(@NotNull WarehouseTransactionOrderPackId id);

  boolean exists(@NotNull WarehouseTransactionOrderId orderId,
    @NotNull ItemId itemId);

  Stream<WarehouseTransactionOrderPack> findAllBy(
    @NotNull WarehouseTransactionOrderId orderId);

  Optional<WarehouseTransactionOrderPack> findBy(@NotNull WarehouseTransactionOrderPackId id);

  void update(@NotNull WarehouseTransactionOrderPack orderPack);

}
