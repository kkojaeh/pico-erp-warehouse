package pico.erp.warehouse.pack;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WarehousePackService {

  WarehousePackData create(@Valid WarehousePackRequests.CreateRequest request);

  void delete(@Valid WarehousePackRequests.DeleteRequest request);

  boolean exists(@NotNull WarehousePackId id);

  WarehousePackData get(@NotNull WarehousePackId id);

  void pack(@Valid WarehousePackRequests.PackRequest request);

  void put(@Valid WarehousePackRequests.PutRequest request);

}
