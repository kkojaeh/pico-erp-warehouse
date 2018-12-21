package pico.erp.warehouse.transaction.request.item;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

public interface TransactionRequestItemService {

  TransactionRequestItemData create(
    @Valid TransactionRequestItemRequests.CreateRequest request);

  void delete(
    @Valid TransactionRequestItemRequests.DeleteRequest request);

  boolean exists(@NotNull TransactionRequestItemId id);

  TransactionRequestItemData get(@NotNull TransactionRequestItemId id);

  List<TransactionRequestItemData> getAll(
    @NotNull TransactionRequestId transactionRequestId);

  void update(@Valid TransactionRequestItemRequests.UpdateRequest request);

}
