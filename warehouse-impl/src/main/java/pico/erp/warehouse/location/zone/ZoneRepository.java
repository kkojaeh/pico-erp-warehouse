package pico.erp.warehouse.location.zone;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.SiteId;

public interface ZoneRepository {

  Zone create(@NotNull Zone warehouseZone);

  void deleteBy(@NotNull ZoneId id);

  boolean exists(@NotNull ZoneId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Zone> findAllBy(@NotNull SiteId siteId);

  Optional<Zone> findBy(@NotNull ZoneId id);

  void update(@NotNull Zone warehouseZone);

}
