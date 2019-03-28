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
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.SiteId;

@Repository
interface ZoneEntityRepository extends
  CrudRepository<ZoneEntity, ZoneId> {

  @Query("SELECT CASE WHEN COUNT(wz) > 0 THEN true ELSE false END FROM Zone wz WHERE wz.locationCode = :locationCode AND wz.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT wz FROM Zone wz WHERE wz.siteId = :siteId AND wz.deleted = false ORDER BY wz.code")
  Stream<ZoneEntity> findAllBy(@Param("siteId") SiteId siteId);

}

@Repository
@Transactional
public class ZoneRepositoryJpa implements ZoneRepository {

  @Autowired
  private ZoneEntityRepository repository;

  @Autowired
  private ZoneMapper mapper;

  @Override
  public Zone create(@NotNull Zone site) {
    val entity = mapper.jpa(site);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull ZoneId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull ZoneId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Zone> findAllBy(@NotNull SiteId siteId) {
    return repository.findAllBy(siteId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Zone> findBy(@NotNull ZoneId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Zone site) {
    val entity = repository.findById(site.getId()).get();
    mapper.pass(mapper.jpa(site), entity);
    repository.save(entity);
  }

}
