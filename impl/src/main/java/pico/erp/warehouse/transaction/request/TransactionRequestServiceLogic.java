package pico.erp.warehouse.transaction.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.ComponentBean;
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
import pico.erp.warehouse.transaction.request.TransactionRequestRequests.AcceptRequest;
import pico.erp.warehouse.transaction.request.TransactionRequestRequests.CancelUncommittedRequest;
import pico.erp.warehouse.transaction.request.TransactionRequestRequests.CompleteRequest;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class TransactionRequestServiceLogic implements TransactionRequestService {

  @Autowired
  private TransactionRequestRepository requestRepository;

  @Autowired
  private TransactionRequestMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public void accept(AcceptRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(TransactionRequestRequests.CancelRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancelUncommitted(CancelUncommittedRequest request) {
    requestRepository.findAllUncommittedAt(request.getFixedDate())
      .forEach(req -> {
        val response = req.apply(
          TransactionRequestMessages.CancelRequest.builder()
            .canceledBy(auditorAware.getCurrentAuditor().get())
            .build()
        );
        requestRepository.update(req);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void commit(TransactionRequestRequests.CommitRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public TransactionRequestData create(
    TransactionRequestRequests.CreateRequest request) {
    val req = new TransactionRequest();
    val response = req.apply(mapper.map(request));
    if (requestRepository.exists(request.getId())) {
      throw new TransactionRequestExceptions.AlreadyExistsException();
    }
    if (requestRepository.exists(req.getCode())) {
      throw new TransactionRequestExceptions.CodeAlreadyExistsException();
    }
    val created = requestRepository.create(req);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(TransactionRequestId id) {
    return requestRepository.exists(id);
  }

  @Override
  public TransactionRequestData get(TransactionRequestId id) {
    return requestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
  }

  @Override
  public void update(TransactionRequestRequests.UpdateRequest request) {
    val req = requestRepository.findBy(request.getId())
      .orElseThrow(TransactionRequestExceptions.NotFoundException::new);
    val response = req.apply(mapper.map(request));
    requestRepository.update(req);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void verify(VerifyRequest request) {
    val aggregator = requestRepository
      .findAggregatorBy(request.getId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new TransactionRequestMessages.VerifyRequest());
      requestRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }


  @Getter
  @Builder
  public static class VerifyRequest {

    @Valid
    @NotNull
    TransactionRequestId id;

  }

}
