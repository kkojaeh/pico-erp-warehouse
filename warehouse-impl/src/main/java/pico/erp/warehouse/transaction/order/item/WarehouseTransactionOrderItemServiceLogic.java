package pico.erp.warehouse.transaction.order.item;

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
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionOrderItemServiceLogic implements
  WarehouseTransactionOrderItemService {

  @Autowired
  private WarehouseTransactionOrderItemRepository orderItemRepository;

  @Autowired
  private WarehouseTransactionOrderItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public WarehouseTransactionOrderItemData create(
    WarehouseTransactionOrderItemRequests.CreateRequest request) {
    if (orderItemRepository
      .exists(request.getOrderId(), request.getItemId())) {
      throw new WarehouseTransactionOrderItemExceptions.AlreadyExistsException();
    }
    val transactionOrder = new WarehouseTransactionOrderItem();
    val response = transactionOrder.apply(mapper.map(request));
    if (orderItemRepository.exists(transactionOrder.getId())) {
      throw new WarehouseTransactionOrderItemExceptions.AlreadyExistsException();
    }

    val created = orderItemRepository.create(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehouseTransactionOrderItemRequests.DeleteRequest request) {
    val transactionOrder = orderItemRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderItemExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderItemRepository.deleteBy(transactionOrder.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WarehouseTransactionOrderItemId id) {
    return orderItemRepository.exists(id);
  }

  @Override
  public WarehouseTransactionOrderItemData get(WarehouseTransactionOrderItemId id) {
    return orderItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionOrderItemExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseTransactionOrderItemData> getAll(
    WarehouseTransactionOrderId transactionOrderId) {
    return orderItemRepository.findAllBy(transactionOrderId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(WarehouseTransactionOrderItemRequests.UpdateRequest request) {
    val transactionOrder = orderItemRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderItemExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderItemRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

}
