package pico.erp.warehouse.transaction.order.item.lot;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;

public interface TransactionOrderItemLotRepository {

  TransactionOrderItemLot create(
    @NotNull TransactionOrderItemLot orderItemLot);

  void deleteBy(@NotNull TransactionOrderItemLotId id);

  boolean exists(@NotNull TransactionOrderItemLotId id);

  boolean exists(@NotNull TransactionOrderItemId orderItemId,
    @NotNull ItemLotId itemLotId);

  Stream<TransactionOrderItemLot> findAllBy(
    @NotNull TransactionOrderItemId orderItemId);

  Stream<TransactionOrderItemLot> findAllBy(
    @NotNull TransactionOrderId orderId);

  Optional<TransactionOrderItemLot> findBy(
    @NotNull TransactionOrderItemLotId id);

  void update(@NotNull TransactionOrderItemLot orderItemLot);

}
