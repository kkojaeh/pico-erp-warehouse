package pico.erp.warehouse.location.bay;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.bay.WarehouseBayExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.bay.WarehouseBayRequests.CreateRequest;
import pico.erp.warehouse.location.bay.WarehouseBayRequests.DeleteRequest;
import pico.erp.warehouse.location.bay.WarehouseBayRequests.UpdateRequest;
import pico.erp.warehouse.location.rack.WarehouseRackId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseBayServiceLogic implements WarehouseBayService {

  @Autowired
  private WarehouseBayRepository warehouseBayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private WarehouseBayMapper mapper;


  @Override
  public WarehouseBayData create(CreateRequest request) {
    val warehouseBay = new WarehouseBay();
    val response = warehouseBay.apply(mapper.map(request));
    if (warehouseBayRepository.exists(warehouseBay.getId())) {
      throw new WarehouseBayExceptions.AlreadyExistsException();
    }
    if (warehouseBayRepository.exists(warehouseBay.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = warehouseBayRepository.create(warehouseBay);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val warehouseBay = warehouseBayRepository.findBy(request.getId())
      .orElseThrow(WarehouseBayExceptions.NotFoundException::new);
    val response = warehouseBay.apply(mapper.map(request));
    warehouseBayRepository.deleteBy(warehouseBay.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehouseBayId id) {
    return warehouseBayRepository.exists(id);
  }

  @Override
  public WarehouseBayData get(@NotNull WarehouseBayId id) {
    return warehouseBayRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseBayExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseBayData> getAll(@NotNull WarehouseRackId rackId) {
    return warehouseBayRepository.findAllBy(rackId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseBay = warehouseBayRepository.findBy(request.getId())
      .orElseThrow(WarehouseBayExceptions.NotFoundException::new);
    val response = warehouseBay.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseBayRepository.exists(warehouseBay.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseBayRepository.update(warehouseBay);
    eventPublisher.publishEvents(response.getEvents());
  }
}
