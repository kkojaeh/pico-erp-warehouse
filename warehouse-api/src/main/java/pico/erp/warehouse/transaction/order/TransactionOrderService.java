package pico.erp.warehouse.transaction.order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface TransactionOrderService {

  void accept(@Valid TransactionOrderRequests.AcceptRequest request);

  void cancel(@Valid TransactionOrderRequests.CancelRequest request);

  void commit(@Valid TransactionOrderRequests.CommitRequest request);

  void complete(@Valid TransactionOrderRequests.CompleteRequest request);

  TransactionOrderData create(
    @Valid TransactionOrderRequests.CreateRequest request);

  boolean exists(@NotNull TransactionOrderId id);

  TransactionOrderData get(@NotNull TransactionOrderId id);

  void update(@Valid TransactionOrderRequests.UpdateRequest request);

}
