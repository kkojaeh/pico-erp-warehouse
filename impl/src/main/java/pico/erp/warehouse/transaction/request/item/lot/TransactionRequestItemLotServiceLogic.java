package pico.erp.warehouse.transaction.request.item.lot;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionRequestItemLotServiceLogic implements
  TransactionRequestItemLotService {

  @Lazy
  @Autowired
  private TransactionRequestItemLotRepository requestItemLotRepository;

  @Autowired
  private TransactionRequestItemLotMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public TransactionRequestItemLotData create(
    TransactionRequestItemLotRequests.CreateRequest request) {
    if (requestItemLotRepository
      .exists(request.getRequestItemId(), request.getItemLotId())) {
      throw new TransactionRequestItemLotExceptions.AlreadyExistsException();
    }
    val itemLot = new TransactionRequestItemLot();
    val response = itemLot.apply(mapper.map(request));
    if (requestItemLotRepository.exists(itemLot.getId())) {
      throw new TransactionRequestItemLotExceptions.AlreadyExistsException();
    }

    val created = requestItemLotRepository.create(itemLot);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionRequestItemLotRequests.DeleteRequest request) {
    val itemLot = requestItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    requestItemLotRepository.deleteBy(itemLot.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(TransactionRequestItemLotId id) {
    return requestItemLotRepository.exists(id);
  }

  @Override
  public TransactionRequestItemLotData get(TransactionRequestItemLotId id) {
    return requestItemLotRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionRequestItemLotExceptions.NotFoundException::new);
  }

  @Override
  public List<TransactionRequestItemLotData> getAll(
    TransactionRequestItemId requestItemId) {
    return requestItemLotRepository.findAllBy(requestItemId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void deleteBy(DeleteByRequestItemRequest request) {
    requestItemLotRepository
      .findAllBy(request.getRequestItemId())
      .forEach(itemLot -> {
        val response = itemLot.apply(
          new TransactionRequestItemLotMessages.DeleteRequest()
        );
        requestItemLotRepository.deleteBy(itemLot.getId());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(TransactionRequestItemLotRequests.UpdateRequest request) {
    val itemLot = requestItemLotRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestItemLotExceptions.NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    requestItemLotRepository.update(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public List<TransactionRequestItemLotData> getAll(TransactionRequestId transactionRequestId) {
    return requestItemLotRepository.findAllBy(transactionRequestId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Getter
  @Builder
  public static class DeleteByRequestItemRequest {

    @Valid
    @NotNull
    TransactionRequestItemId requestItemId;

  }

}
