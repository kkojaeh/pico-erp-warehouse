package pico.erp.warehouse.transaction;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface TransactionRepository {

  Transaction create(@NotNull Transaction warehouseTransaction);

  void deleteBy(@NotNull TransactionId id);

  boolean exists(@NotNull TransactionId id);

  Optional<Transaction> findBy(@NotNull TransactionId id);

  void update(@NotNull Transaction warehouseTransaction);

}
