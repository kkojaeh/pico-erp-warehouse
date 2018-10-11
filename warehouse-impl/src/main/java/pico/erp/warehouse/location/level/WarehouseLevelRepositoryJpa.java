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
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.bay.WarehouseBayId;

@Repository
interface WarehouseLevelEntityRepository extends
  CrudRepository<WarehouseLevelEntity, WarehouseLevelId> {

  @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM WarehouseLevel wl WHERE wl.locationCode = :locationCode")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

  @Query("SELECT wl FROM WarehouseLevel wl WHERE wl.bay.id = :bayId ORDER BY wl.code")
  Stream<WarehouseLevelEntity> findAllBy(@Param("bayId") WarehouseBayId bayId);

}

@Repository
@Transactional
public class WarehouseLevelRepositoryJpa implements WarehouseLevelRepository {

  @Autowired
  private WarehouseLevelEntityRepository repository;

  @Autowired
  private WarehouseLevelMapper mapper;

  @Override
  public WarehouseLevel create(@NotNull WarehouseLevel bay) {
    val entity = mapper.entity(bay);
    val created = repository.save(entity);
    return mapper.domain(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseLevelId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLevelId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<WarehouseLevel> findAllBy(@NotNull WarehouseBayId bayId) {
    return repository.findAllBy(bayId)
      .map(mapper::domain);
  }

  @Override
  public Optional<WarehouseLevel> findBy(@NotNull WarehouseLevelId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::domain);
  }

  @Override
  public void update(@NotNull WarehouseLevel rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.entity(rack), entity);
    repository.save(entity);
  }

}
