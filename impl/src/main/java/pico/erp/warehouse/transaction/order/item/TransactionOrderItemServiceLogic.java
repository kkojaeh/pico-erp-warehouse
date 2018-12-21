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
import pico.erp.warehouse.transaction.order.TransactionOrderId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionOrderItemServiceLogic implements
  TransactionOrderItemService {

  @Autowired
  private TransactionOrderItemRepository orderItemRepository;

  @Autowired
  private TransactionOrderItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public TransactionOrderItemData create(
    TransactionOrderItemRequests.CreateRequest request) {
    if (orderItemRepository
      .exists(request.getOrderId(), request.getItemId())) {
      throw new TransactionOrderItemExceptions.AlreadyExistsException();
    }
    val orderItem = new TransactionOrderItem();
    val response = orderItem.apply(mapper.map(request));
    if (orderItemRepository.exists(orderItem.getId())) {
      throw new TransactionOrderItemExceptions.AlreadyExistsException();
    }

    val created = orderItemRepository.create(orderItem);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionOrderItemRequests.DeleteRequest request) {
    val orderItem = orderItemRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemExceptions.NotFoundException::new);
    val response = orderItem.apply(mapper.map(request));
    orderItemRepository.deleteBy(orderItem.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(TransactionOrderItemId id) {
    return orderItemRepository.exists(id);
  }

  @Override
  public TransactionOrderItemData get(TransactionOrderItemId id) {
    return orderItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionOrderItemExceptions.NotFoundException::new);
  }

  @Override
  public List<TransactionOrderItemData> getAll(
    TransactionOrderId transactionOrderId) {
    return orderItemRepository.findAllBy(transactionOrderId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(TransactionOrderItemRequests.UpdateRequest request) {
    val orderItem = orderItemRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemExceptions.NotFoundException::new);
    val response = orderItem.apply(mapper.map(request));
    orderItemRepository.update(orderItem);
    eventPublisher.publishEvents(response.getEvents());
  }

}
