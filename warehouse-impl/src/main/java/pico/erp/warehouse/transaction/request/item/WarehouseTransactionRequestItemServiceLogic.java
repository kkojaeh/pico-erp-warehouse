package pico.erp.warehouse.transaction.request.item;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionRequestItemServiceLogic implements
  WarehouseTransactionRequestItemService {

  @Autowired
  private WarehouseTransactionRequestItemRepository warehouseTransactionRequestRepository;

  @Autowired
  private WarehouseTransactionRequestItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public WarehouseTransactionRequestItemData create(
    WarehouseTransactionRequestItemRequests.CreateRequest request) {
    if (warehouseTransactionRequestRepository
      .exists(request.getRequestId(), request.getItemId())) {
      throw new WarehouseTransactionRequestItemExceptions.AlreadyExistsException();
    }
    val transactionRequest = new WarehouseTransactionRequestItem();
    val response = transactionRequest.apply(mapper.map(request));
    if (warehouseTransactionRequestRepository.exists(transactionRequest.getId())) {
      throw new WarehouseTransactionRequestItemExceptions.AlreadyExistsException();
    }

    val created = warehouseTransactionRequestRepository.create(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(WarehouseTransactionRequestItemRequests.DeleteRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestItemExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.deleteBy(transactionRequest.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(WarehouseTransactionRequestItemId id) {
    return warehouseTransactionRequestRepository.exists(id);
  }

  @Override
  public WarehouseTransactionRequestItemData get(WarehouseTransactionRequestItemId id) {
    return warehouseTransactionRequestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionRequestItemExceptions.NotFoundException::new);
  }

  @Override
  public List<WarehouseTransactionRequestItemData> getAll(
    WarehouseTransactionRequestId transactionRequestId) {
    return warehouseTransactionRequestRepository.findAllBy(transactionRequestId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(WarehouseTransactionRequestItemRequests.UpdateRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestItemExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

}
