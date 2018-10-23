package pico.erp.warehouse.transaction.request;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.WarehouseTransactionId;

public interface WarehouseTransactionRequestRepository {

  long countByCreatedToday();

  WarehouseTransactionRequest create(
    @NotNull WarehouseTransactionRequest warehouseTransactionRequest);

  void deleteBy(@NotNull WarehouseTransactionId id);

  boolean exists(@NotNull WarehouseTransactionId id);

  Optional<WarehouseTransactionRequest> findBy(@NotNull WarehouseTransactionId id);

  void update(@NotNull WarehouseTransactionRequest warehouseTransactionRequest);

}
