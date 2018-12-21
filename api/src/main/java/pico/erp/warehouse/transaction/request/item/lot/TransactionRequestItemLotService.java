package pico.erp.warehouse.transaction.request.item.lot;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;

public interface TransactionRequestItemLotService {

  TransactionRequestItemLotData create(
    @Valid TransactionRequestItemLotRequests.CreateRequest request);

  void delete(
    @Valid TransactionRequestItemLotRequests.DeleteRequest request);

  boolean exists(@NotNull TransactionRequestItemLotId id);

  TransactionRequestItemLotData get(@NotNull TransactionRequestItemLotId id);

  List<TransactionRequestItemLotData> getAll(
    @NotNull TransactionRequestItemId transactionRequestItemId);

  List<TransactionRequestItemLotData> getAll(
    @NotNull TransactionRequestId transactionRequestId);

  void update(@Valid TransactionRequestItemLotRequests.UpdateRequest request);

}
