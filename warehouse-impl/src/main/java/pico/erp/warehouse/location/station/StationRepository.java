package pico.erp.warehouse.location.station;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.SiteId;

public interface StationRepository {

  Station create(@NotNull Station warehouseStation);

  void deleteBy(@NotNull StationId id);

  boolean exists(@NotNull StationId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Station> findAllBy(@NotNull SiteId siteId);

  Optional<Station> findBy(@NotNull StationId id);

  void update(@NotNull Station warehouseStation);

}
