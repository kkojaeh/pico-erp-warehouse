package pico.erp.warehouse.location.level;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.warehouse.location.bay.BayId;

public interface LevelService {

  LevelData create(@Valid LevelRequests.CreateRequest request);

  void delete(@Valid LevelRequests.DeleteRequest request);

  boolean exists(@NotNull LevelId id);

  LevelData get(@NotNull LevelId id);

  List<LevelData> getAll(@NotNull BayId bayId);

  void update(@Valid LevelRequests.UpdateRequest request);

}
