package pico.erp.warehouse.transaction.request.item.lot;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;

public interface TransactionRequestItemLotRepository {

  TransactionRequestItemLot create(
    @NotNull TransactionRequestItemLot requestItemLot);

  void deleteBy(@NotNull TransactionRequestItemLotId id);

  boolean exists(@NotNull TransactionRequestItemLotId id);

  boolean exists(@NotNull TransactionRequestItemId requestItemId,
    @NotNull ItemLotId itemLotId);

  Stream<TransactionRequestItemLot> findAllBy(
    @NotNull TransactionRequestItemId requestItemId);

  Stream<TransactionRequestItemLot> findAllBy(
    @NotNull TransactionRequestId requestId);

  Optional<TransactionRequestItemLot> findBy(
    @NotNull TransactionRequestItemLotId id);

  void update(@NotNull TransactionRequestItemLot requestItemLot);

}
