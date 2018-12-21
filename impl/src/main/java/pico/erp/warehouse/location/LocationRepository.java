package pico.erp.warehouse.location;

import java.util.Optional;
import javax.validation.constraints.NotNull;

public interface LocationRepository {

  Location create(@NotNull Location warehouseLocation);

  void deleteBy(@NotNull LocationId id);

  boolean exists(@NotNull LocationId id);

  boolean exists(@NotNull LocationCode locationCode);

  Optional<Location> findBy(@NotNull LocationId id);

  void update(@NotNull Location warehouseLocation);

}
