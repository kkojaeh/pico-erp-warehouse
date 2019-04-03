package pico.erp.warehouse.transaction.request;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface TransactionRequestRepository {

  TransactionRequest create(
    @NotNull TransactionRequest warehouseTransactionRequest);

  void deleteBy(@NotNull TransactionRequestId id);

  boolean exists(@NotNull TransactionRequestId id);

  boolean exists(@NotNull TransactionRequestCode code);

  Optional<TransactionRequestAggregator> findAggregatorBy(
    @NotNull TransactionRequestId id);

  long countCreatedBetween(LocalDateTime begin, LocalDateTime end);

  Optional<TransactionRequest> findBy(@NotNull TransactionRequestId id);

  void update(@NotNull TransactionRequest warehouseTransactionRequest);

  Stream<TransactionRequest> findAllUncommittedAt(@NotNull LocalDateTime fixedDate);

}
