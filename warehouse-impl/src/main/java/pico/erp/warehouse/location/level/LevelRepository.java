package pico.erp.warehouse.location.level;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.bay.BayId;

public interface LevelRepository {

  Level create(@NotNull Level warehouseLevel);

  void deleteBy(@NotNull LevelId id);

  boolean exists(@NotNull LevelId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Level> findAllBy(@NotNull BayId bayId);

  Optional<Level> findBy(@NotNull LevelId id);

  void update(@NotNull Level warehouseLevel);

}
