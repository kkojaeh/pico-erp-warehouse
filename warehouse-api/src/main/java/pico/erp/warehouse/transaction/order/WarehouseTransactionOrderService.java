package pico.erp.warehouse.transaction.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionOrderService {

  WarehouseTransactionOrderData create(
    @Valid WarehouseTransactionOrderRequests.CreateRequest request);

  boolean exists(@NotNull WarehouseTransactionOrderId id);

  WarehouseTransactionOrderData get(@NotNull WarehouseTransactionOrderId id);

  void update(@Valid WarehouseTransactionOrderRequests.UpdateRequest request);

  void accept(@Valid WarehouseTransactionOrderRequests.AcceptRequest request);

  void cancel(@Valid WarehouseTransactionOrderRequests.CancelRequest request);

  void commit(@Valid WarehouseTransactionOrderRequests.CommitRequest request);

  void complete(@Valid WarehouseTransactionOrderRequests.CompleteRequest request);

}
