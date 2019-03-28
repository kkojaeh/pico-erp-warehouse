package pico.erp.warehouse.transaction.request.item;

import java.util.List;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class TransactionRequestItemServiceLogic implements
  TransactionRequestItemService {

  @Autowired
  private TransactionRequestItemRepository requestItemRepository;

  @Autowired
  private TransactionRequestItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public TransactionRequestItemData create(
    TransactionRequestItemRequests.CreateRequest request) {
    if (requestItemRepository
      .exists(request.getRequestId(), request.getItemId())) {
      throw new TransactionRequestItemExceptions.AlreadyExistsException();
    }
    val item = new TransactionRequestItem();
    val response = item.apply(mapper.map(request));
    if (requestItemRepository.exists(request.getId())) {
      throw new TransactionRequestItemExceptions.AlreadyExistsException();
    }

    val created = requestItemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(TransactionRequestItemRequests.DeleteRequest request) {
    val item = requestItemRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    requestItemRepository.deleteBy(request.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(TransactionRequestItemId id) {
    return requestItemRepository.exists(id);
  }

  @Override
  public TransactionRequestItemData get(TransactionRequestItemId id) {
    return requestItemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionRequestItemExceptions.NotFoundException::new);
  }

  @Override
  public List<TransactionRequestItemData> getAll(
    TransactionRequestId requestId) {
    return requestItemRepository.findAllBy(requestId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(TransactionRequestItemRequests.UpdateRequest request) {
    val item = requestItemRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    requestItemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

}
