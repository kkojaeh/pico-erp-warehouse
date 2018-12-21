package pico.erp.warehouse.transaction.order.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.TransactionOrderId;

public interface TransactionOrderItemService {

  TransactionOrderItemData create(
    @Valid TransactionOrderItemRequests.CreateRequest request);

  void delete(
    @Valid TransactionOrderItemRequests.DeleteRequest request);

  boolean exists(@NotNull TransactionOrderItemId id);

  TransactionOrderItemData get(@NotNull TransactionOrderItemId id);

  List<TransactionOrderItemData> getAll(
    @NotNull TransactionOrderId transactionOrderId);

  void update(@Valid TransactionOrderItemRequests.UpdateRequest request);

}
