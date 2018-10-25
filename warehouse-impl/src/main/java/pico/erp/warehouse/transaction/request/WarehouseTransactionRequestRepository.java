package pico.erp.warehouse.transaction.request;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionRequestRepository {

  WarehouseTransactionRequest create(
    @NotNull WarehouseTransactionRequest warehouseTransactionRequest);

  void deleteBy(@NotNull WarehouseTransactionRequestId id);

  boolean exists(@NotNull WarehouseTransactionRequestId id);

  Stream<WarehouseTransactionRequest> findAllUncommittedAt(@NotNull OffsetDateTime fixedDate);

  void update(@NotNull WarehouseTransactionRequest warehouseTransactionRequest);

  Optional<WarehouseTransactionRequest> findBy(@NotNull WarehouseTransactionRequestId id);

}
