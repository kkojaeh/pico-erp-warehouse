package pico.erp.warehouse.transaction.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehouseTransactionRequestService {

  WarehouseTransactionRequestData create(
    @Valid WarehouseTransactionRequestRequests.CreateRequest request);

  void accept(@Valid WarehouseTransactionRequestRequests.AcceptRequest request);

  void cancel(@Valid WarehouseTransactionRequestRequests.CancelRequest request);

  boolean exists(@NotNull WarehouseTransactionRequestId id);

  WarehouseTransactionRequestData get(@NotNull WarehouseTransactionRequestId id);

  void update(@Valid WarehouseTransactionRequestRequests.UpdateRequest request);

  void cancelUncommitted(
    @Valid WarehouseTransactionRequestRequests.CancelUncommittedRequest request);

  void commit(@Valid WarehouseTransactionRequestRequests.CommitRequest request);

  void complete(@Valid WarehouseTransactionRequestRequests.CompleteRequest request);

}
