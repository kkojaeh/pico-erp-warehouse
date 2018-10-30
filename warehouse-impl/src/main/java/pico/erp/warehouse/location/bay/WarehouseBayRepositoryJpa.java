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
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.rack.WarehouseRackId;

@Repository
interface WarehouseBayEntityRepository extends
  CrudRepository<WarehouseBayEntity, WarehouseBayId> {

  @Query("SELECT CASE WHEN COUNT(wb) > 0 THEN true ELSE false END FROM WarehouseBay wb WHERE wb.locationCode = :locationCode AND wb.deleted = false")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

  @Query("SELECT wb FROM WarehouseBay wb WHERE wb.rackId = :rackId AND wb.deleted = false ORDER BY wb.code")
  Stream<WarehouseBayEntity> findAllBy(@Param("rackId") WarehouseRackId rackId);

}

@Repository
@Transactional
public class WarehouseBayRepositoryJpa implements WarehouseBayRepository {

  @Autowired
  private WarehouseBayEntityRepository repository;

  @Autowired
  private WarehouseBayMapper mapper;

  @Override
  public WarehouseBay create(@NotNull WarehouseBay bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseBayId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseBayId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<WarehouseBay> findAllBy(@NotNull WarehouseRackId rackId) {
    return repository.findAllBy(rackId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseBay> findBy(@NotNull WarehouseBayId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseBay rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
