package pico.erp.warehouse.pack;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.location.station.StationEntity;


@Repository
interface PackEntityRepository extends
  CrudRepository<PackEntity, PackId> {

  @Query("SELECT COUNT(p) FROM Pack p WHERE p.createdDate >= :begin AND p.createdDate <= :end")
  long countByCreatedDateBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pack p WHERE p.code = :code")
  boolean exists(@Param("code") PackCode code);

  @Query("SELECT p FROM Pack p WHERE p.itemLotId = :itemLotId ORDER BY p.createdDate")
  Stream<StationEntity> findAllBy(@Param("itemLotId") ItemLotId itemLotId);

}

@Repository
@Transactional
public class PackRepositoryJpa implements PackRepository {

  @Autowired
  private PackEntityRepository repository;

  @Autowired
  private PackMapper mapper;

  @Override
  public long countByCreatedToday() {
    val begin = ZonedDateTime.now().with(LocalTime.MIN).toOffsetDateTime();
    val end = ZonedDateTime.now().with(LocalTime.MAX).toOffsetDateTime();
    return repository.countByCreatedDateBetween(begin, end);
  }

  @Override
  public Pack create(@NotNull Pack bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull PackId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull PackCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public boolean exists(@NotNull PackId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<Pack> findAllBy(ItemLotId itemLotId) {
    return null;
  }

  @Override
  public Optional<Pack> findBy(@NotNull PackId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull Pack rack) {
    val entity = repository.findOne(rack.getId());
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
