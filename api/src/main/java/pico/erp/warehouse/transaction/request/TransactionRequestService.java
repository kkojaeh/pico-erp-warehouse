package pico.erp.warehouse.transaction.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface TransactionRequestService {

  void accept(@Valid TransactionRequestRequests.AcceptRequest request);

  void cancel(@Valid TransactionRequestRequests.CancelRequest request);

  void cancelUncommitted(
    @Valid TransactionRequestRequests.CancelUncommittedRequest request);

  void commit(@Valid TransactionRequestRequests.CommitRequest request);

  void complete(@Valid TransactionRequestRequests.CompleteRequest request);

  TransactionRequestData create(
    @Valid TransactionRequestRequests.CreateRequest request);

  boolean exists(@NotNull TransactionRequestId id);

  TransactionRequestData get(@NotNull TransactionRequestId id);

  void update(@Valid TransactionRequestRequests.UpdateRequest request);

}
