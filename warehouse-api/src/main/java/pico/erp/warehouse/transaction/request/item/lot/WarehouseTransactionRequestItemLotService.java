package pico.erp.warehouse.transaction.request.item.lot;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;

public interface WarehouseTransactionRequestItemLotService {

  WarehouseTransactionRequestItemLotData create(
    @Valid WarehouseTransactionRequestItemLotRequests.CreateRequest request);

  void delete(
    @Valid WarehouseTransactionRequestItemLotRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionRequestItemLotId id);

  WarehouseTransactionRequestItemLotData get(@NotNull WarehouseTransactionRequestItemLotId id);

  List<WarehouseTransactionRequestItemLotData> getAll(
    @NotNull WarehouseTransactionRequestItemId transactionRequestItemId);

  void update(@Valid WarehouseTransactionRequestItemLotRequests.UpdateRequest request);

}
