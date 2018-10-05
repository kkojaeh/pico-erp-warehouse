package pico.erp.warehouse.location.level;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.bay.WarehouseBayId;

public interface WarehouseLevelService {

  WarehouseLevelData create(@Valid WarehouseLevelRequests.CreateRequest request);

  void delete(@Valid WarehouseLevelRequests.DeleteRequest request);

  boolean exists(@NotNull WarehouseLevelId id);

  WarehouseLevelData get(@NotNull WarehouseLevelId id);

  List<WarehouseLevelData> getAll(@NotNull WarehouseBayId bayId);

  void update(@Valid WarehouseLevelRequests.UpdateRequest request);

}
