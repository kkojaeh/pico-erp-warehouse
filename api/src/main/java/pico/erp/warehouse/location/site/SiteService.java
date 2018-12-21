package pico.erp.warehouse.location.site;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface SiteService {

  SiteData create(@Valid SiteRequests.CreateRequest request);

  void delete(@Valid SiteRequests.DeleteRequest request);

  boolean exists(@NotNull SiteId id);

  SiteData get(@NotNull SiteId id);

  List<SiteData> getAll();

  void update(@Valid SiteRequests.UpdateRequest request);

}
