package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

public interface TransactionOrderRepository {

  TransactionOrder create(
    @NotNull TransactionOrder warehouseTransactionOrder);

  void deleteBy(@NotNull TransactionOrderId id);

  boolean exists(@NotNull TransactionOrderId id);

  Optional<TransactionOrderAggregator> findAggregatorBy(
    @NotNull TransactionOrderId id);

  Stream<TransactionOrder> findAllUncommittedAt(@NotNull OffsetDateTime fixedDate);

  Optional<TransactionOrder> findBy(@NotNull TransactionOrderId id);

  Optional<TransactionOrder> findBy(@NotNull TransactionRequestId requestId);

  void update(@NotNull TransactionOrder warehouseTransactionOrder);

}