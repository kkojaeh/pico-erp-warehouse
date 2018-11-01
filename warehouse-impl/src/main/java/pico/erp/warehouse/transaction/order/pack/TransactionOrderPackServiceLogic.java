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
import pico.erp.warehouse.transaction.order.TransactionOrderId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionOrderPackServiceLogic implements
  TransactionOrderPackService {

  @Autowired
  private TransactionOrderPackRepository orderPackRepository;

  @Autowired
  private TransactionOrderPackMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public TransactionOrderPackData create(
    TransactionOrderPackRequests.CreateRequest request) {
    if (orderPackRepository
      .exists(request.getOrderId(), request.getPackId())) {
      throw new TransactionOrderPackExceptions.AlreadyExistsException();
    }
    val transactionOrder = new TransactionOrderPack();
    val response = transactionOrder.apply(mapper.map(request));
    if (orderPackRepository.exists(transactionOrder.getId())) {
      throw new TransactionOrderPackExceptions.AlreadyExistsException();
    }

    val created = orderPackRepository.create(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionOrderPackRequests.DeleteRequest request) {
    val transactionOrder = orderPackRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderPackExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderPackRepository.deleteBy(transactionOrder.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(TransactionOrderPackId id) {
    return orderPackRepository.exists(id);
  }

  @Override
  public TransactionOrderPackData get(TransactionOrderPackId id) {
    return orderPackRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionOrderPackExceptions.NotFoundException::new);
  }

  @Override
  public List<TransactionOrderPackData> getAll(
    TransactionOrderId transactionOrderId) {
    return orderPackRepository.findAllBy(transactionOrderId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(TransactionOrderPackRequests.UpdateRequest request) {
    val transactionOrder = orderPackRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderPackExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderPackRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

}
