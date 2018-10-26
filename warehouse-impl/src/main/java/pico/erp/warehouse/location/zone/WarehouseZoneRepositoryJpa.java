package pico.erp.warehouse.location.zone;

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
interface WarehouseZoneEntityRepository extends
  CrudRepository<WarehouseZoneEntity, WarehouseZoneId> {

  @Query("SELECT CASE WHEN COUNT(wz) > 0 THEN true ELSE false END FROM WarehouseZone wz WHERE wz.locationCode = :locationCode AND wz.deleted = false")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

  @Query("SELECT wz FROM WarehouseZone wz WHERE wz.siteId = :siteId AND wz.deleted = false ORDER BY wz.code")
  Stream<WarehouseZoneEntity> findAllBy(@Param("siteId") WarehouseSiteId siteId);

}

@Repository
@Transactional
public class WarehouseZoneRepositoryJpa implements WarehouseZoneRepository {

  @Autowired
  private WarehouseZoneEntityRepository repository;

  @Autowired
  private WarehouseZoneMapper mapper;

  @Override
  public WarehouseZone create(@NotNull WarehouseZone site) {
    val entity = mapper.jpa(site);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseZoneId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseZoneId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<WarehouseZone> findAllBy(@NotNull WarehouseSiteId siteId) {
    return repository.findAllBy(siteId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseZone> findBy(@NotNull WarehouseZoneId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseZone site) {
    val entity = repository.findOne(site.getId());
    mapper.pass(mapper.jpa(site), entity);
    repository.save(entity);
  }

}
