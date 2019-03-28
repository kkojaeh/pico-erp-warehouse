package pico.erp.warehouse.transaction.order.item.lot;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.Give;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;

@SuppressWarnings("Duplicates")
@Service
@Give
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
    val orderItemLot = new TransactionOrderItemLot();
    val response = orderItemLot.apply(mapper.map(request));
    if (orderItemLotRepository.exists(orderItemLot.getId())) {
      throw new TransactionOrderItemLotExceptions.AlreadyExistsException();
    }

    val created = orderItemLotRepository.create(orderItemLot);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionOrderItemLotRequests.DeleteRequest request) {
    val orderItemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemLotExceptions.NotFoundException::new);
    val response = orderItemLot.apply(mapper.map(request));
    orderItemLotRepository.deleteBy(orderItemLot.getId());
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
    val orderItemLot = orderItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderItemLotExceptions.NotFoundException::new);
    val response = orderItemLot.apply(mapper.map(request));
    orderItemLotRepository.update(orderItemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteByOrderItemRequest request) {
    orderItemLotRepository
      .findAllBy(request.getOrderItemId())
      .forEach(itemLot -> {
        val response = itemLot.apply(
          new TransactionOrderItemLotMessages.DeleteRequest()
        );
        orderItemLotRepository.deleteBy(itemLot.getId());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Getter
  @Builder
  public static class DeleteByOrderItemRequest {

    @Valid
    @NotNull
    TransactionOrderItemId orderItemId;

  }

}
