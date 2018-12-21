package pico.erp.warehouse.transaction.order.pack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.pack.PackId;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

public interface TransactionOrderPackRepository {

  TransactionOrderPack create(
    @NotNull TransactionOrderPack orderPack);

  void deleteBy(@NotNull TransactionOrderPackId id);

  boolean exists(@NotNull TransactionOrderPackId id);

  boolean exists(@NotNull TransactionOrderId orderId,
    @NotNull PackId packId);

  Stream<TransactionOrderPack> findAllBy(
    @NotNull TransactionOrderId orderId);

  Optional<TransactionOrderPack> findBy(@NotNull TransactionOrderPackId id);

  void update(@NotNull TransactionOrderPack orderPack);

}
