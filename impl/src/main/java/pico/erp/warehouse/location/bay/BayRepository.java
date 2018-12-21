package pico.erp.warehouse.location.bay;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.rack.RackId;

public interface BayRepository {

  Bay create(@NotNull Bay warehouseBay);

  void deleteBy(@NotNull BayId id);

  boolean exists(@NotNull BayId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Bay> findAllBy(@NotNull RackId rackId);

  Optional<Bay> findBy(@NotNull BayId id);

  void update(@NotNull Bay warehouseBay);

}
