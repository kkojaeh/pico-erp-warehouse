package pico.erp.warehouse.location.site;

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

@Repository
interface SiteEntityRepository extends
  CrudRepository<SiteEntity, SiteId> {

  @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM Site ws WHERE ws.locationCode = :locationCode AND ws.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT ws FROM Site ws WHERE ws.deleted = false ORDER BY ws.code")
  Stream<SiteEntity> findAllBy();

}

@Repository
@Transactional
public class SiteRepositoryJpa implements SiteRepository {

  @Autowired
  private SiteEntityRepository repository;

  @Autowired
  private SiteMapper mapper;

  @Override
  public Site create(@NotNull Site site) {
    val entity = mapper.jpa(site);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull SiteId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull SiteId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Site> findAll() {
    return repository.findAllBy()
      .map(mapper::jpa);
  }

  @Override
  public Optional<Site> findBy(@NotNull SiteId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Site site) {
    val entity = repository.findOne(site.getId());
    mapper.pass(mapper.jpa(site), entity);
    repository.save(entity);
  }

}
