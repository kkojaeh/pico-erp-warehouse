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
public class WarehouseLocationServiceLogic implements WarehouseLocationService {

  @Autowired
  private WarehouseLocationRepository warehouseLocationRepository;

  @Autowired
  private WarehouseLocationMapper mapper;

  @Override
  public boolean exists(@NotNull WarehouseLocationId id) {
    return warehouseLocationRepository.exists(id);
  }

  @Override
  public WarehouseLocationData get(@NotNull WarehouseLocationId id) {
    return warehouseLocationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseLocationExceptions.NotFoundException::new);
  }
}
