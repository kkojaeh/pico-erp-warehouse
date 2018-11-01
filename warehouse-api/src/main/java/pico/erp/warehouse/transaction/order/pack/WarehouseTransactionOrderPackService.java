package pico.erp.warehouse.transaction.order.pack;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

public interface WarehouseTransactionOrderPackService {

  WarehouseTransactionOrderPackData create(
    @Valid WarehouseTransactionOrderPackRequests.CreateRequest request);

  void delete(
    @Valid WarehouseTransactionOrderPackRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseTransactionOrderPackId id);

  WarehouseTransactionOrderPackData get(@NotNull WarehouseTransactionOrderPackId id);

  List<WarehouseTransactionOrderPackData> getAll(
    @NotNull WarehouseTransactionOrderId transactionOrderId);

  void update(@Valid WarehouseTransactionOrderPackRequests.UpdateRequest request);

}
