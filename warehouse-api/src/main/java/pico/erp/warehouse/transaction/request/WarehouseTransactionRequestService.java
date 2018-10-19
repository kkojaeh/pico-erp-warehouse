package pico.erp.warehouse.transaction.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionRequestService {

  WarehouseTransactionRequestData create(
    @Valid WarehouseTransactionRequestRequests.CreateRequest request);

  void delete(@Valid WarehouseTransactionRequestRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionRequestId id);

  WarehouseTransactionRequestData get(@NotNull WarehouseTransactionRequestId id);

  void update(@Valid WarehouseTransactionRequestRequests.UpdateRequest request);

}
