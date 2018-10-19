package pico.erp.warehouse.transaction;

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
interface WarehouseTransactionEntityRepository extends
  CrudRepository<WarehouseTransactionEntity, WarehouseTransactionId> {

  @Query("SELECT COUNT(wp) FROM WarehousePack wp WHERE wp.createdDate >= :begin AND wp.createdDate <= :end")
  long countByCreatedDateBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

}

@Repository
@Transactional
public class WarehouseTransactionRepositoryJpa implements WarehouseTransactionRepository {

  @Autowired
  private WarehouseTransactionEntityRepository repository;

  @Autowired
  private WarehouseTransactionMapper mapper;

  @Override
  public long countByCreatedToday() {
    val begin = ZonedDateTime.now().with(LocalTime.MIN).toOffsetDateTime();
    val end = ZonedDateTime.now().with(LocalTime.MAX).toOffsetDateTime();
    return repository.countByCreatedDateBetween(begin, end);
  }

  @Override
  public WarehouseTransaction create(@NotNull WarehouseTransaction bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<WarehouseTransaction> findBy(@NotNull WarehouseTransactionId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransaction rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
