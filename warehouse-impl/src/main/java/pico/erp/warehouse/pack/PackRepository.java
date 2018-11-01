package pico.erp.warehouse.pack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotId;

public interface PackRepository {

  long countByCreatedToday();

  Pack create(@NotNull Pack warehousePack);

  void deleteBy(@NotNull PackId id);

  boolean exists(@NotNull PackCode packCode);

  boolean exists(@NotNull PackId id);

  Stream<Pack> findAllBy(ItemLotId itemLotId);

  Optional<Pack> findBy(@NotNull PackId id);

  void update(@NotNull Pack warehousePack);

}
