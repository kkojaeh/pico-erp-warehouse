package pico.erp.warehouse.location.bay;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.rack.RackId;

@Repository
interface BayEntityRepository extends
  CrudRepository<BayEntity, BayId> {

  @Query("SELECT CASE WHEN COUNT(wb) > 0 THEN true ELSE false END FROM Bay wb WHERE wb.locationCode = :locationCode AND wb.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT wb FROM Bay wb WHERE wb.rackId = :rackId AND wb.deleted = false ORDER BY wb.code")
  Stream<BayEntity> findAllBy(@Param("rackId") RackId rackId);

}

@Repository
@Transactional
public class BayRepositoryJpa implements BayRepository {

  @Autowired
  private BayEntityRepository repository;

  @Autowired
  private BayMapper mapper;

  @Override
  public Bay create(@NotNull Bay bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull BayId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull BayId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Bay> findAllBy(@NotNull RackId rackId) {
    return repository.findAllBy(rackId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Bay> findBy(@NotNull BayId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Bay rack) {
    val entity = repository.findById(rack.getId()).get();
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
