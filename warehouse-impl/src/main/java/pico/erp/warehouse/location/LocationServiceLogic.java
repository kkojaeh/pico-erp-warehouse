package pico.erp.warehouse.location;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class LocationServiceLogic implements LocationService {

  @Autowired
  private LocationRepository warehouseLocationRepository;

  @Autowired
  private LocationMapper mapper;

  @Override
  public boolean exists(@NotNull LocationId id) {
    return warehouseLocationRepository.exists(id);
  }

  @Override
  public LocationData get(@NotNull LocationId id) {
    return warehouseLocationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(LocationExceptions.NotFoundException::new);
  }
}
