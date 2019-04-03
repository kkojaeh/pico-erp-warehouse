package pico.erp.warehouse.location.station;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.location.site.SiteId;
import pico.erp.warehouse.location.station.StationExceptions.CodeAlreadyExistsException;
import pico.erp.warehouse.location.station.StationRequests.CreateRequest;
import pico.erp.warehouse.location.station.StationRequests.DeleteRequest;
import pico.erp.warehouse.location.station.StationRequests.UpdateRequest;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class StationServiceLogic implements StationService {

  @Autowired
  private StationRepository stationRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private StationMapper mapper;

  @Override
  public StationData create(CreateRequest request) {
    val station = new Station();
    val response = station.apply(mapper.map(request));
    if (stationRepository.exists(station.getId())) {
      throw new StationExceptions.AlreadyExistsException();
    }
    if (stationRepository.exists(station.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = stationRepository.create(station);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val station = stationRepository.findBy(request.getId())
      .orElseThrow(StationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    stationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }

  public void deleteBy(DeleteBySiteRequest request) {
    stationRepository.findAllBy(request.getSiteId())
      .map(rack -> new StationRequests.DeleteRequest(rack.getId()))
      .forEach(this::delete);
  }

  @Override
  public boolean exists(@NotNull StationId id) {
    return stationRepository.exists(id);
  }

  @Override
  public StationData get(@NotNull StationId id) {
    return stationRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(StationExceptions.NotFoundException::new);
  }

  @Override
  public List<StationData> getAll(@NotNull SiteId siteId) {
    return stationRepository.findAllBy(siteId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  public void resetCodeBy(ResetCodeBySiteRequest request) {
    stationRepository.findAllBy(request.getSiteId())
      .forEach(bay -> {
        val response = bay.apply(new StationMessages.ResetLocationCodeRequest());
        stationRepository.update(bay);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public void update(UpdateRequest request) {
    val station = stationRepository.findBy(request.getId())
      .orElseThrow(StationExceptions.NotFoundException::new);
    val response = station.apply(mapper.map(request));
    if (response.isCodeChanged() && stationRepository
      .exists(station.getLocationCode())) {
      throw new CodeAlreadyExistsException();
    }
    stationRepository.update(station);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class DeleteBySiteRequest {

    @Valid
    @NotNull
    SiteId siteId;

  }

  @Getter
  @Builder
  public static class ResetCodeBySiteRequest {

    @Valid
    @NotNull
    SiteId siteId;

  }

}
