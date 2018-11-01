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
  private WarehouseTransactionRequestRepository requestRepository;

  @Autowired
  private WarehouseTransactionRequestMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public void accept(AcceptRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(WarehouseTransactionRequestRequests.CancelRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancelUncommitted(CancelUncommittedRequest request) {
    requestRepository.findAllUncommittedAt(request.getFixedDate())
      .forEach(req -> {
        val response = req.apply(
          WarehouseTransactionRequestMessages.CancelRequest.builder()
            .canceledBy(auditorAware.getCurrentAuditor())
            .build()
        );
        requestRepository.update(req);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void commit(WarehouseTransactionRequestRequests.CommitRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public WarehouseTransactionRequestData create(
    WarehouseTransactionRequestRequests.CreateRequest request) {
    val req = new WarehouseTransactionRequest();
    val response = req.apply(mapper.map(request));
    if (requestRepository.exists(request.getId())) {
      throw new WarehouseTransactionRequestExceptions.AlreadyExistsException();
    }
    val created = requestRepository.create(req);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(WarehouseTransactionRequestId id) {
    return requestRepository.exists(id);
  }

  @Override
  public WarehouseTransactionRequestData get(WarehouseTransactionRequestId id) {
    return requestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
  }

  @Override
  public void update(WarehouseTransactionRequestRequests.UpdateRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

}
