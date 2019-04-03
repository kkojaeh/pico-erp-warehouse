package pico.erp.warehouse.location.rack;

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
import pico.erp.warehouse.location.zone.ZoneId;

@Repository
interface RackEntityRepository extends
  CrudRepository<RackEntity, RackId> {

  @Query("SELECT CASE WHEN COUNT(wr) > 0 THEN true ELSE false END FROM Rack wr WHERE wr.locationCode = :locationCode AND wr.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT wr FROM Rack wr WHERE wr.zoneId = :zoneId AND wr.deleted = false ORDER BY wr.code")
  Stream<RackEntity> findAllBy(@Param("zoneId") ZoneId zoneId);

}

@Repository
@Transactional
public class RackRepositoryJpa implements RackRepository {

  @Autowired
  private RackEntityRepository repository;

  @Autowired
  private RackMapper mapper;

  @Override
  public Rack create(@NotNull Rack rack) {
    val entity = mapper.jpa(rack);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull RackId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull RackId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Rack> findAllBy(@NotNull ZoneId zoneId) {
    return repository.findAllBy(zoneId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Rack> findBy(@NotNull RackId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Rack rack) {
    val entity = repository.findById(rack.getId()).get();
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
