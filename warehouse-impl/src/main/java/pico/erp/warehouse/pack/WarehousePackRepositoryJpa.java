package pico.erp.warehouse.pack;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
interface WarehousePackEntityRepository extends
  CrudRepository<WarehousePackEntity, WarehousePackId> {

  @Query("SELECT COUNT(wp) FROM WarehousePack wp WHERE wp.createdDate >= :begin AND wp.createdDate <= :end")
  long countByCreatedDateBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT CASE WHEN COUNT(wp) > 0 THEN true ELSE false END FROM WarehousePack wp WHERE wp.code = :code")
  boolean exists(@Param("code") WarehousePackCode code);

}

@Repository
@Transactional
public class WarehousePackRepositoryJpa implements WarehousePackRepository {

  @Autowired
  private WarehousePackEntityRepository repository;

  @Autowired
  private WarehousePackMapper mapper;

  @Override
  public long countByCreatedToday() {
    val begin = ZonedDateTime.now().with(LocalTime.MIN).toOffsetDateTime();
    val end = ZonedDateTime.now().with(LocalTime.MAX).toOffsetDateTime();
    return repository.countByCreatedDateBetween(begin, end);
  }

  @Override
  public WarehousePack create(@NotNull WarehousePack bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehousePackId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehousePackCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public boolean exists(@NotNull WarehousePackId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<WarehousePack> findBy(@NotNull WarehousePackId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehousePack rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
