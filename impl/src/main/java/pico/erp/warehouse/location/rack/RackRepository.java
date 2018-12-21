package pico.erp.warehouse.location.rack;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.zone.ZoneId;

public interface RackRepository {

  Rack create(@NotNull Rack warehouseRack);

  void deleteBy(@NotNull RackId id);

  boolean exists(@NotNull RackId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Rack> findAllBy(@NotNull ZoneId zoneId);

  Optional<Rack> findBy(@NotNull RackId id);

  void update(@NotNull Rack warehouseRack);

}
