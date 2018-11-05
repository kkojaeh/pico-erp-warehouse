package pico.erp.warehouse.pack;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.Value;
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotId;

public interface PackRepository {

  long countByCreatedToday();

  Pack create(@NotNull Pack warehousePack);

  void deleteBy(@NotNull PackId id);

  boolean exists(@NotNull PackCode packCode);

  boolean exists(@NotNull PackId id);

  Stream<Pack> findAllBy(ItemLotId itemLotId);

  Optional<Pack> findBy(@NotNull PackId id);

  Stream<ItemLotQuantity> findItemLotQuantityAllBy(ItemId itemId);

  void update(@NotNull Pack warehousePack);

  @Value
  class ItemLotQuantity {

    ItemLotId itemLotId;

    BigDecimal quantity;

  }

}
