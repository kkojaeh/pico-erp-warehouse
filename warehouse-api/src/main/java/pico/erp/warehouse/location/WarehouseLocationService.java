package pico.erp.warehouse.location;

import javax.validation.constraints.NotNull;

public interface WarehouseLocationService {

  boolean exists(@NotNull WarehouseLocationId id);

  WarehouseLocationData get(@NotNull WarehouseLocationId id);

}
