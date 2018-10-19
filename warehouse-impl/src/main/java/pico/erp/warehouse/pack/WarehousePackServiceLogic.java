package pico.erp.warehouse.pack;

import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehousePackServiceLogic implements WarehousePackService {

  @Autowired
  private WarehousePackRepository warehousePackRepository;

  @Autowired
  private WarehousePackMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;


  @Override
  public WarehousePackData create(WarehousePackRequests.CreateRequest request) {
    val pack = new WarehousePack();
    val response = pack.apply(mapper.map(request));
    if (warehousePackRepository.exists(pack.getId())) {
      throw new WarehousePackExceptions.AlreadyExistsException();
    }
    if (warehousePackRepository.exists(pack.getCode())) {
      throw new WarehousePackExceptions.CodeAlreadyExistsException();
    }
    val created = warehousePackRepository.create(pack);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehousePackRequests.DeleteRequest request) {
    val pack = warehousePackRepository.findBy(request.getId())
      .orElseThrow(WarehousePackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    warehousePackRepository.deleteBy(pack.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull WarehousePackId id) {
    return warehousePackRepository.exists(id);
  }

  @Override
  public WarehousePackData get(@NotNull WarehousePackId id) {
    return warehousePackRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehousePackExceptions.NotFoundException::new);
  }

  @Override
  public void pack(WarehousePackRequests.PackRequest request) {
    val pack = warehousePackRepository.findBy(request.getId())
      .orElseThrow(WarehousePackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    warehousePackRepository.update(pack);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void put(WarehousePackRequests.PutRequest request) {
    val pack = warehousePackRepository.findBy(request.getId())
      .orElseThrow(WarehousePackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    warehousePackRepository.update(pack);
    eventPublisher.publishEvents(response.getEvents());
  }


}
