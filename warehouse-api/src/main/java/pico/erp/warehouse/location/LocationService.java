package pico.erp.warehouse.location;

import javax.validation.constraints.NotNull;

public interface LocationService {

  boolean exists(@NotNull LocationId id);

  LocationData get(@NotNull LocationId id);

}
