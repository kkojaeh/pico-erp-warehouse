package pico.erp.warehouse.transaction.order.pack;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

public interface TransactionOrderPackService {

  TransactionOrderPackData create(
    @Valid TransactionOrderPackRequests.CreateRequest request);

  void delete(
    @Valid TransactionOrderPackRequests.DeleteRequest request);

  boolean exists(@NotNull TransactionOrderPackId id);

  TransactionOrderPackData get(@NotNull TransactionOrderPackId id);

  List<TransactionOrderPackData> getAll(
    @NotNull TransactionOrderId transactionOrderId);

  void update(@Valid TransactionOrderPackRequests.UpdateRequest request);

}
