package pico.erp.warehouse.transaction.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionOrderService {

  WarehouseTransactionOrderData create(
    @Valid WarehouseTransactionOrderRequests.CreateRequest request);

  void delete(@Valid WarehouseTransactionOrderRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionOrderId id);

  WarehouseTransactionOrderData get(@NotNull WarehouseTransactionOrderId id);

  void update(@Valid WarehouseTransactionOrderRequests.UpdateRequest request);

}
