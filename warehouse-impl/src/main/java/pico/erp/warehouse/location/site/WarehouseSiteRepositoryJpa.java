package pico.erp.warehouse.location.site;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.warehouse.location.WarehouseLocationCode;

@Repository
interface WarehouseSiteEntityRepository extends
  CrudRepository<WarehouseSiteEntity, WarehouseSiteId> {

  @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM WarehouseSite ws WHERE ws.locationCode = :locationCode")
  boolean exists(@Param("locationCode") WarehouseLocationCode locationCode);

}

@Repository
@Transactional
public class WarehouseSiteRepositoryJpa implements WarehouseSiteRepository {

  @Autowired
  private WarehouseSiteEntityRepository repository;

  @Autowired
  private WarehouseSiteMapper mapper;

  @Override
  public WarehouseSite create(@NotNull WarehouseSite site) {
    val entity = mapper.entity(site);
    val created = repository.save(entity);
    return mapper.domain(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseSiteId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseSiteId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseLocationCode locationCode) {
    return repository.exists(locationCode);
  }


  @Override
  public Optional<WarehouseSite> findBy(@NotNull WarehouseSiteId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::domain);
  }

  @Override
  public void update(@NotNull WarehouseSite site) {
    val entity = repository.findOne(site.getId());
    mapper.pass(mapper.entity(site), entity);
    repository.save(entity);
  }

}
