package pico.erp.warehouse.transaction.order.pack;

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
public class WarehouseTransactionOrderPackServiceLogic implements
  WarehouseTransactionOrderPackService {

  @Autowired
  private WarehouseTransactionOrderPackRepository orderItemRepository;

  @Autowired
  private WarehouseTransactionOrderPackMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public WarehouseTransactionOrderPackData create(
    WarehouseTransactionOrderPackRequests.CreateRequest request) {
    if (orderItemRepository
      .exists(request.getOrderId(), request.getItemId())) {
      throw new WarehouseTransactionOrderPackExceptions.AlreadyExistsException();
    }
    val transactionOrder = new WarehouseTransactionOrderPack();
    val response = transactionOrder.apply(mapper.map(request));
    if (orderItemRepository.exists(transactionOrder.getId())) {
      throw new WarehouseTransactionOrderPackExceptions.AlreadyExistsException();
    }

    val created = orderItemRepository.create(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehouseTransactionOrderPackRequests.DeleteRequest request) {
    val transactionOrder = orderItemRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderPackExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderItemRepository.deleteBy(transactionOrder.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WarehouseTransactionOrderPackId id) {
    return orderItemRepository.exists(id);
  }

  @Override
  public WarehouseTransactionOrderPackData get(WarehouseTransactionOrderPackId id) {
    return orderItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionOrderPackExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseTransactionOrderPackData> getAll(
    WarehouseTransactionOrderId transactionOrderId) {
    return orderItemRepository.findAllBy(transactionOrderId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(WarehouseTransactionOrderPackRequests.UpdateRequest request) {
    val transactionOrder = orderItemRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderPackExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderItemRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

}
