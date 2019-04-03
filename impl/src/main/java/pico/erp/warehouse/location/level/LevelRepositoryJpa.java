package pico.erp.warehouse.location.level;

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
import pico.erp.warehouse.location.bay.BayId;

@Repository
interface LevelEntityRepository extends
  CrudRepository<LevelEntity, LevelId> {

  @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM Level wl WHERE wl.locationCode = :locationCode AND wl.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT wl FROM Level wl WHERE wl.bayId = :bayId AND wl.deleted = false ORDER BY wl.code")
  Stream<LevelEntity> findAllBy(@Param("bayId") BayId bayId);

}

@Repository
@Transactional
public class LevelRepositoryJpa implements LevelRepository {

  @Autowired
  private LevelEntityRepository repository;

  @Autowired
  private LevelMapper mapper;

  @Override
  public Level create(@NotNull Level bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull LevelId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull LevelId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Level> findAllBy(@NotNull BayId bayId) {
    return repository.findAllBy(bayId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Level> findBy(@NotNull LevelId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Level rack) {
    val entity = repository.findById(rack.getId()).get();
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
