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
import pico.erp.warehouse.location.bay.BayExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.bay.BayRequests.CreateRequest;
import pico.erp.warehouse.location.bay.BayRequests.DeleteRequest;
import pico.erp.warehouse.location.bay.BayRequests.UpdateRequest;
import pico.erp.warehouse.location.rack.RackId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class BayServiceLogic implements BayService {

  @Autowired
  private BayRepository warehouseBayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private BayMapper mapper;


  @Override
  public BayData create(CreateRequest request) {
    val warehouseBay = new Bay();
    val response = warehouseBay.apply(mapper.map(request));
    if (warehouseBayRepository.exists(warehouseBay.getId())) {
      throw new BayExceptions.AlreadyExistsException();
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
      .orElseThrow(BayExceptions.NotFoundException::new);
    val response = warehouseBay.apply(mapper.map(request));
    warehouseBayRepository.update(warehouseBay);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull BayId id) {
    return warehouseBayRepository.exists(id);
  }

  @Override
  public BayData get(@NotNull BayId id) {
    return warehouseBayRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(BayExceptions.NotFoundException::new);
  }

  @Override
  public List<BayData> getAll(@NotNull RackId rackId) {
    return warehouseBayRepository.findAllBy(rackId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val warehouseBay = warehouseBayRepository.findBy(request.getId())
      .orElseThrow(BayExceptions.NotFoundException::new);
    val response = warehouseBay.apply(mapper.map(request));
    if (response.isCodeChanged() && warehouseBayRepository.exists(warehouseBay.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    warehouseBayRepository.update(warehouseBay);
    eventPublisher.publishEvents(response.getEvents());
  }
}