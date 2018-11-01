package pico.erp.warehouse.transaction.order.item.lot;

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
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionOrderItemLotServiceLogic implements
  WarehouseTransactionOrderItemLotService {

  @Autowired
  private WarehouseTransactionOrderItemLotRepository orderItemLotRepository;

  @Autowired
  private WarehouseTransactionOrderItemLotMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public WarehouseTransactionOrderItemLotData create(
    WarehouseTransactionOrderItemLotRequests.CreateRequest request) {
    if (orderItemLotRepository
      .exists(request.getOrderItemId(), request.getItemLotId())) {
      throw new WarehouseTransactionOrderItemLotExceptions.AlreadyExistsException();
    }
    val itemLot = new WarehouseTransactionOrderItemLot();
    val response = itemLot.apply(mapper.map(request));
    if (orderItemLotRepository.exists(itemLot.getId())) {
      throw new WarehouseTransactionOrderItemLotExceptions.AlreadyExistsException();
    }

    val created = orderItemLotRepository.create(itemLot);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehouseTransactionOrderItemLotRequests.DeleteRequest request) {
    val itemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    orderItemLotRepository.deleteBy(itemLot.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WarehouseTransactionOrderItemLotId id) {
    return orderItemLotRepository.exists(id);
  }

  @Override
  public WarehouseTransactionOrderItemLotData get(WarehouseTransactionOrderItemLotId id) {
    return orderItemLotRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionOrderItemLotExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseTransactionOrderItemLotData> getAll(
    WarehouseTransactionOrderItemId transactionOrderItemId) {
    return orderItemLotRepository.findAllBy(transactionOrderItemId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(WarehouseTransactionOrderItemLotRequests.UpdateRequest request) {
    val itemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    orderItemLotRepository.update(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

}
