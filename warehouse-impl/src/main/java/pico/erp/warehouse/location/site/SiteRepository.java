package pico.erp.warehouse.location.site;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.LocationCode;

public interface SiteRepository {

  Site create(@NotNull Site warehouseSite);

  void deleteBy(@NotNull SiteId id);

  boolean exists(@NotNull SiteId id);

  boolean exists(@NotNull LocationCode locationCode);

  Stream<Site> findAll();

  Optional<Site> findBy(@NotNull SiteId id);

  void update(@NotNull Site warehouseSite);

}
