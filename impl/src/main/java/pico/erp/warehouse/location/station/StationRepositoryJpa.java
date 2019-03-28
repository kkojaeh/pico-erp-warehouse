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
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.SiteId;

@Repository
interface StationEntityRepository extends
  CrudRepository<StationEntity, StationId> {

  @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END FROM Station ws WHERE ws.locationCode = :locationCode AND ws.deleted = false")
  boolean exists(@Param("locationCode") LocationCode locationCode);

  @Query("SELECT ws FROM Station ws WHERE ws.siteId = :siteId AND ws.deleted = false ORDER BY ws.code")
  Stream<StationEntity> findAllBy(@Param("siteId") SiteId siteId);

}

@Repository
@Transactional
public class StationRepositoryJpa implements StationRepository {

  @Autowired
  private StationEntityRepository repository;

  @Autowired
  private StationMapper mapper;

  @Override
  public Station create(@NotNull Station site) {
    val entity = mapper.jpa(site);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull StationId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull StationId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Stream<Station> findAllBy(@NotNull SiteId siteId) {
    return repository.findAllBy(siteId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Station> findBy(@NotNull StationId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Station site) {
    val entity = repository.findById(site.getId()).get();
    mapper.pass(mapper.jpa(site), entity);
    repository.save(entity);
  }

}
