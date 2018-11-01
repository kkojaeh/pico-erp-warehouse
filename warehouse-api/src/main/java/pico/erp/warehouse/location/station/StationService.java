package pico.erp.warehouse.location.station;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.site.SiteId;

public interface StationService {

  StationData create(@Valid StationRequests.CreateRequest request);

  void delete(@Valid StationRequests.DeleteRequest request);

  boolean exists(@NotNull StationId id);

  StationData get(@NotNull StationId id);

  List<StationData> getAll(@NotNull SiteId siteId);

  void update(@Valid StationRequests.UpdateRequest request);

}
