package pico.erp.warehouse.transaction.request.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;

public interface WarehouseTransactionRequestItemService {

  WarehouseTransactionRequestItemData create(
    @Valid WarehouseTransactionRequestItemRequests.CreateRequest request);

  void delete(
    @Valid WarehouseTransactionRequestItemRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionRequestItemId id);

  WarehouseTransactionRequestItemData get(@NotNull WarehouseTransactionRequestItemId id);

  List<WarehouseTransactionRequestItemData> getAll(
    @NotNull WarehouseTransactionRequestId transactionRequestId);

  void update(@Valid WarehouseTransactionRequestItemRequests.UpdateRequest request);

}
