package pico.erp.warehouse.transaction.request.item.lot;

import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionRequestItemLotServiceLogic implements
  WarehouseTransactionRequestItemLotService {

  @Autowired
  private WarehouseTransactionRequestItemLotRepository warehouseTransactionRequestItemLotRepository;

  @Autowired
  private WarehouseTransactionRequestItemLotMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public WarehouseTransactionRequestItemLotData create(
    WarehouseTransactionRequestItemLotRequests.CreateRequest request) {
    if (warehouseTransactionRequestItemLotRepository
      .exists(request.getRequestItemId(), request.getItemLotId())) {
      throw new WarehouseTransactionRequestItemLotExceptions.AlreadyExistsException();
    }
    val itemLot = new WarehouseTransactionRequestItemLot();
    val response = itemLot.apply(mapper.map(request));
    if (warehouseTransactionRequestItemLotRepository.exists(itemLot.getId())) {
      throw new WarehouseTransactionRequestItemLotExceptions.AlreadyExistsException();
    }

    val created = warehouseTransactionRequestItemLotRepository.create(itemLot);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehouseTransactionRequestItemLotRequests.DeleteRequest request) {
    val itemLot = warehouseTransactionRequestItemLotRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    warehouseTransactionRequestItemLotRepository.deleteBy(itemLot.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WarehouseTransactionRequestItemLotId id) {
    return warehouseTransactionRequestItemLotRepository.exists(id);
  }

  @Override
  public WarehouseTransactionRequestItemLotData get(WarehouseTransactionRequestItemLotId id) {
    return warehouseTransactionRequestItemLotRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionRequestItemLotExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseTransactionRequestItemLotData> getAll(
    WarehouseTransactionRequestItemId transactionRequestItemId) {
    return warehouseTransactionRequestItemLotRepository.findAllBy(transactionRequestItemId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(WarehouseTransactionRequestItemLotRequests.UpdateRequest request) {
    val itemLot = warehouseTransactionRequestItemLotRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    warehouseTransactionRequestItemLotRepository.update(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

}
