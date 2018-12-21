package pico.erp.warehouse.transaction.order.item.lot;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;

public interface TransactionOrderItemLotService {

  TransactionOrderItemLotData create(
    @Valid TransactionOrderItemLotRequests.CreateRequest request);

  void delete(
    @Valid TransactionOrderItemLotRequests.DeleteRequest request);

  boolean exists(@NotNull TransactionOrderItemLotId id);

  TransactionOrderItemLotData get(@NotNull TransactionOrderItemLotId id);

  List<TransactionOrderItemLotData> getAll(
    @NotNull TransactionOrderItemId transactionRequestItemId);

  void update(@Valid TransactionOrderItemLotRequests.UpdateRequest request);

}
