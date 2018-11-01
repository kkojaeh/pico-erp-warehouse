package pico.erp.warehouse.transaction.order.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

public interface WarehouseTransactionOrderItemService {

  WarehouseTransactionOrderItemData create(
    @Valid WarehouseTransactionOrderItemRequests.CreateRequest request);

  void delete(
    @Valid WarehouseTransactionOrderItemRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionOrderItemId id);

  WarehouseTransactionOrderItemData get(@NotNull WarehouseTransactionOrderItemId id);

  List<WarehouseTransactionOrderItemData> getAll(
    @NotNull WarehouseTransactionOrderId transactionOrderId);

  void update(@Valid WarehouseTransactionOrderItemRequests.UpdateRequest request);

}
