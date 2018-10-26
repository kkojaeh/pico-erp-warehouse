package pico.erp.warehouse.location.station;

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
import pico.erp.warehouse.location.site.WarehouseSiteId;

@Repository
interface WarehouseStationEntityRepository extends
  CrudRepository<WarehouseStationEntity, WarehouseStationId> {

  @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM WarehouseStation ws WHERE ws.locationCode = :locationCode AND ws.deleted = false")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

  @Query("SELECT ws FROM WarehouseStation ws WHERE ws.siteId = :siteId AND ws.deleted = false ORDER BY ws.code")
  Stream<WarehouseStationEntity> findAllBy(@Param("siteId") WarehouseSiteId siteId);

}

@Repository
@Transactional
public class WarehouseStationRepositoryJpa implements WarehouseStationRepository {

  @Autowired
  private WarehouseStationEntityRepository repository;

  @Autowired
  private WarehouseStationMapper mapper;

  @Override
  public WarehouseStation create(@NotNull WarehouseStation site) {
    val entity = mapper.jpa(site);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseStationId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseStationId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<WarehouseStation> findAllBy(@NotNull WarehouseSiteId siteId) {
    return repository.findAllBy(siteId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseStation> findBy(@NotNull WarehouseStationId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseStation site) {
    val entity = repository.findOne(site.getId());
    mapper.pass(mapper.jpa(site), entity);
    repository.save(entity);
  }

}
