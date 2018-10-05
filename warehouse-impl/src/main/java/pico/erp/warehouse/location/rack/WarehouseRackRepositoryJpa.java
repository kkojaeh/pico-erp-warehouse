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
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.zone.WarehouseZoneId;

@Repository
interface WarehouseRackEntityRepository extends
  CrudRepository<WarehouseRackEntity, WarehouseRackId> {

  @Query("SELECT CASE WHEN COUNT(wr) > 0 THEN true ELSE false END FROM WarehouseRack wr WHERE wr.locationCode = :locationCode")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

  @Query("SELECT wr FROM WarehouseRack wr WHERE wr.zone.id = :zoneId")
  Stream<WarehouseRackEntity> findAllBy(@Param("zoneId") WarehouseZoneId zoneId);

}

@Repository
@Transactional
public class WarehouseRackRepositoryJpa implements WarehouseRackRepository {

  @Autowired
  private WarehouseRackEntityRepository repository;

  @Autowired
  private WarehouseRackMapper mapper;

  @Override
  public WarehouseRack create(@NotNull WarehouseRack rack) {
    val entity = mapper.entity(rack);
    val created = repository.save(entity);
    return mapper.domain(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseRackId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseRackId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<WarehouseRack> findAllBy(@NotNull WarehouseZoneId zoneId) {
    return repository.findAllBy(zoneId)
      .map(mapper::domain);
  }

  @Override
  public Optional<WarehouseRack> findBy(@NotNull WarehouseRackId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::domain);
  }

  @Override
  public void update(@NotNull WarehouseRack rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.entity(rack), entity);
    repository.save(entity);
  }

}
