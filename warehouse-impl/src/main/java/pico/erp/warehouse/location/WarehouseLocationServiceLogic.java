package pico.erp.warehouse.location;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.warehouse.location.level.WarehouseLevelRepository;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseLocationServiceLogic implements WarehouseLocationService {

  @Autowired
  private WarehouseLevelRepository warehouseLevelRepository;

  @Autowired
  private WarehouseLocationMapper mapper;

  @Override
  public boolean exists(@NotNull WarehouseLocationId id) {
    return warehouseLevelRepository.exists(mapper.map(id));
  }

  @Override
  public WarehouseLocationData get(@NotNull WarehouseLocationId id) {
    return warehouseLevelRepository.findBy(mapper.map(id))
      .map(mapper::map)
      .orElseThrow(WarehouseLocationExceptions.NotFoundException::new);
  }
}
