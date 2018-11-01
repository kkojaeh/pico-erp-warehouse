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
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionOrderItemLotServiceLogic implements
  TransactionOrderItemLotService {

  @Autowired
  private TransactionOrderItemLotRepository orderItemLotRepository;

  @Autowired
  private TransactionOrderItemLotMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public TransactionOrderItemLotData create(
    TransactionOrderItemLotRequests.CreateRequest request) {
    if (orderItemLotRepository
      .exists(request.getOrderItemId(), request.getItemLotId())) {
      throw new TransactionOrderItemLotExceptions.AlreadyExistsException();
    }
    val itemLot = new TransactionOrderItemLot();
    val response = itemLot.apply(mapper.map(request));
    if (orderItemLotRepository.exists(itemLot.getId())) {
      throw new TransactionOrderItemLotExceptions.AlreadyExistsException();
    }

    val created = orderItemLotRepository.create(itemLot);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionOrderItemLotRequests.DeleteRequest request) {
    val itemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    orderItemLotRepository.deleteBy(itemLot.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(TransactionOrderItemLotId id) {
    return orderItemLotRepository.exists(id);
  }

  @Override
  public TransactionOrderItemLotData get(TransactionOrderItemLotId id) {
    return orderItemLotRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionOrderItemLotExceptions.NotFoundException::new);
  }

  @Override
  public List<TransactionOrderItemLotData> getAll(
    TransactionOrderItemId transactionOrderItemId) {
    return orderItemLotRepository.findAllBy(transactionOrderItemId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(TransactionOrderItemLotRequests.UpdateRequest request) {
    val itemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    orderItemLotRepository.update(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

}
