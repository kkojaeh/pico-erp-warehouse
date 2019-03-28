package pico.erp.warehouse.location.bay;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.Give;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.bay.BayExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.bay.BayRequests.CreateRequest;
import pico.erp.warehouse.location.bay.BayRequests.DeleteRequest;
import pico.erp.warehouse.location.bay.BayRequests.UpdateRequest;
import pico.erp.warehouse.location.rack.RackId;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class BayServiceLogic implements BayService {

  @Autowired
  private BayRepository bayRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private BayMapper mapper;

  @Override
  public BayData create(CreateRequest request) {
    val bay = new Bay();
    val response = bay.apply(mapper.map(request));
    if (bayRepository.exists(bay.getId())) {
      throw new BayExceptions.AlreadyExistsException();
    }
    if (bayRepository.exists(bay.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = bayRepository.create(bay);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val bay = bayRepository.findBy(request.getId())
      .orElseThrow(BayExceptions.NotFoundException::new);
    val response = bay.apply(mapper.map(request));
    bayRepository.update(bay);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteByRackRequest request) {
    bayRepository.findAllBy(request.getRackId())
      .map(bay -> new BayRequests.DeleteRequest(bay.getId()))
      .forEach(this::delete);
  }

  @Override
  public boolean exists(@NotNull BayId id) {
    return bayRepository.exists(id);
  }

  @Override
  public BayData get(@NotNull BayId id) {
    return bayRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(BayExceptions.NotFoundException::new);
  }

  @Override
  public List<BayData> getAll(@NotNull RackId rackId) {
    return bayRepository.findAllBy(rackId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void resetCodeBy(ResetCodeByRackRequest request) {
    bayRepository.findAllBy(request.getRackId())
      .forEach(bay -> {
        val response = bay.apply(new BayMessages.ResetLocationCodeRequest());
        bayRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val bay = bayRepository.findBy(request.getId())
      .orElseThrow(BayExceptions.NotFoundException::new);
    val response = bay.apply(mapper.map(request));
    if (response.isCodeChanged() && bayRepository.exists(bay.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    bayRepository.update(bay);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class DeleteByRackRequest {

    @Valid
    @NotNull
    RackId rackId;

  }

  @Getter
  @Builder
  public static class ResetCodeByRackRequest {

    @Valid
    @NotNull
    RackId rackId;

  }


}
