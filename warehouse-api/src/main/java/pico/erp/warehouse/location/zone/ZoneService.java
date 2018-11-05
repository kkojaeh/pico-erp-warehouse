package pico.erp.warehouse.location.zone;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.site.SiteId;

public interface ZoneService {

  ZoneData create(@Valid ZoneRequests.CreateRequest request);

  void delete(@Valid ZoneRequests.DeleteRequest request);

  boolean exists(@NotNull ZoneId id);

  ZoneData get(@NotNull ZoneId id);

  List<ZoneData> getAll(@NotNull SiteId siteId);

  void update(@Valid ZoneRequests.UpdateRequest request);

}
