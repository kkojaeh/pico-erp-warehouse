package pico.erp.warehouse.location;

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
interface LocationEntityRepository extends
  CrudRepository<LocationEntity, LocationId> {

  @Query("SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM Location wl WHERE wl.code = :code AND wl.deleted = false")
  boolean exists(@Param("code") LocationCode code);

}

@Repository
@Transactional
public class LocationRepositoryJpa implements LocationRepository {

  @Autowired
  private LocationEntityRepository repository;

  @Autowired
  private LocationMapper mapper;

  @Override
  public Location create(@NotNull Location bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull LocationId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull LocationId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(@NotNull LocationCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public Optional<Location> findBy(@NotNull LocationId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Location rack) {
    val entity = repository.findById(rack.getId()).get();
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
