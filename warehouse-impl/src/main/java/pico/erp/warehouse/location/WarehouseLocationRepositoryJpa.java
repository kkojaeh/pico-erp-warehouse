package pico.erp.warehouse.location;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface WarehouseLocationEntityRepository extends
  CrudRepository<WarehouseLocationEntity, WarehouseLocationId> {

  @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM WarehouseLocation wl WHERE wl.code = :code AND wl.deleted = false")
  boolean exists(@Param("code") WarehouseLocationCode code);

}

@Repository
@Transactional
public class WarehouseLocationRepositoryJpa implements WarehouseLocationRepository {

  @Autowired
  private WarehouseLocationEntityRepository repository;

  @Autowired
  private WarehouseLocationMapper mapper;

  @Override
  public WarehouseLocation create(@NotNull WarehouseLocation bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseLocationId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Optional<WarehouseLocation> findBy(@NotNull WarehouseLocationId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseLocation rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
