package pico.erp.warehouse.transaction.request;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestRequests.AcceptRequest;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestRequests.CancelUncommittedRequest;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestRequests.CompleteRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionRequestServiceLogic implements WarehouseTransactionRequestService {

  @Autowired
  private WarehouseTransactionRequestRepository warehouseTransactionRequestRepository;

  @Autowired
  private WarehouseTransactionRequestMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public void accept(AcceptRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(WarehouseTransactionRequestRequests.CancelRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancelUncommitted(CancelUncommittedRequest request) {
    warehouseTransactionRequestRepository.findAllUncommittedAt(request.getFixedDate())
      .forEach(transactionRequest -> {
        val response = transactionRequest.apply(
          WarehouseTransactionRequestMessages.CancelRequest.builder()
            .canceledBy(auditorAware.getCurrentAuditor())
            .build()
        );
        warehouseTransactionRequestRepository.update(transactionRequest);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void commit(WarehouseTransactionRequestRequests.CommitRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public WarehouseTransactionRequestData create(
    WarehouseTransactionRequestRequests.CreateRequest request) {
    val transactionRequest = new WarehouseTransactionRequest();
    val response = transactionRequest.apply(mapper.map(request));
    if (warehouseTransactionRequestRepository.exists(transactionRequest.getId())) {
      throw new WarehouseTransactionRequestExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionRequestRepository.create(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(WarehouseTransactionRequestId id) {
    return warehouseTransactionRequestRepository.exists(id);
  }

  @Override
  public WarehouseTransactionRequestData get(WarehouseTransactionRequestId id) {
    return warehouseTransactionRequestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
  }

  @Override
  public void update(WarehouseTransactionRequestRequests.UpdateRequest request) {
    val transactionRequest = warehouseTransactionRequestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = transactionRequest.apply(mapper.map(request));
    warehouseTransactionRequestRepository.update(transactionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

}
