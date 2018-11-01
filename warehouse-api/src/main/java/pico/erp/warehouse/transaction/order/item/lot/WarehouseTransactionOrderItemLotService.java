package pico.erp.warehouse.transaction.order.item.lot;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;

public interface WarehouseTransactionOrderItemLotService {

  WarehouseTransactionOrderItemLotData create(
    @Valid WarehouseTransactionOrderItemLotRequests.CreateRequest request);

  void delete(
    @Valid WarehouseTransactionOrderItemLotRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionOrderItemLotId id);

  WarehouseTransactionOrderItemLotData get(@NotNull WarehouseTransactionOrderItemLotId id);

  List<WarehouseTransactionOrderItemLotData> getAll(
    @NotNull WarehouseTransactionOrderItemId transactionRequestItemId);

  void update(@Valid WarehouseTransactionOrderItemLotRequests.UpdateRequest request);

}
