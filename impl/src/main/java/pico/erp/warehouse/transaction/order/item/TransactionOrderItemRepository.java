package pico.erp.warehouse.transaction.order.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

public interface TransactionOrderItemRepository {

  TransactionOrderItem create(
    @NotNull TransactionOrderItem orderItem);

  void deleteBy(@NotNull TransactionOrderItemId id);

  boolean exists(@NotNull TransactionOrderItemId id);

  boolean exists(@NotNull TransactionOrderId orderId,
    @NotNull ItemId itemId);

  Stream<TransactionOrderItem> findAllBy(
    @NotNull TransactionOrderId orderId);

  Optional<TransactionOrderItem> findBy(@NotNull TransactionOrderItemId id);

  void update(@NotNull TransactionOrderItem orderItem);

}
