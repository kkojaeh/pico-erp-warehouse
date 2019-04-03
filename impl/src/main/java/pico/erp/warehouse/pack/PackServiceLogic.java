package pico.erp.warehouse.pack;

import javax.validation.constraints.NotNull;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class PackServiceLogic implements PackService {

  @Autowired
  private PackRepository packRepository;

  @Autowired
  private PackMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;


  @Override
  public PackData create(PackRequests.CreateRequest request) {
    val pack = new Pack();
    val response = pack.apply(mapper.map(request));
    if (packRepository.exists(pack.getId())) {
      throw new PackExceptions.AlreadyExistsException();
    }
    if (packRepository.exists(pack.getCode())) {
      throw new PackExceptions.CodeAlreadyExistsException();
    }
    val created = packRepository.create(pack);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(PackRequests.DeleteRequest request) {
    val pack = packRepository.findBy(request.getId())
      .orElseThrow(PackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    packRepository.deleteBy(pack.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(@NotNull PackId id) {
    return packRepository.exists(id);
  }

  @Override
  public PackData get(@NotNull PackId id) {
    return packRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(PackExceptions.NotFoundException::new);
  }

  @Override
  public void pack(PackRequests.PackRequest request) {
    val pack = packRepository.findBy(request.getId())
      .orElseThrow(PackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    packRepository.update(pack);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void put(PackRequests.PutRequest request) {
    val pack = packRepository.findBy(request.getId())
      .orElseThrow(PackExceptions.NotFoundException::new);
    val response = pack.apply(mapper.map(request));
    packRepository.update(pack);
    eventPublisher.publishEvents(response.getEvents());
  }


}
