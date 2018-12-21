package pico.erp.warehouse.transaction.request.item;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemId;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

public interface TransactionRequestItemRepository {

  TransactionRequestItem create(
    @NotNull TransactionRequestItem warehouseTransactionRequestItem);

  void deleteBy(@NotNull TransactionRequestItemId id);

  boolean exists(@NotNull TransactionRequestItemId id);

  boolean exists(@NotNull TransactionRequestId requestId,
    @NotNull ItemId itemId);

  Stream<TransactionRequestItem> findAllBy(
    @NotNull TransactionRequestId requestId);

  Optional<TransactionRequestItem> findBy(@NotNull TransactionRequestItemId id);

  void update(@NotNull TransactionRequestItem warehouseTransactionRequestItem);

}
