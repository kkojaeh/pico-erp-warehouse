package pico.erp.warehouse.pack;

import java.time.LocalDateTime;
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
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.pack.PackRepository.ItemLotQuantity;


@Repository
interface PackEntityRepository extends
  CrudRepository<PackEntity, PackId> {

  @Query("SELECT COUNT(p) FROM Pack p WHERE p.createdDate >= :begin AND p.createdDate <= :end")
  long countCreatedBetween(@Param("begin") LocalDateTime begin,
    @Param("end") LocalDateTime end);

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pack p WHERE p.code = :code")
  boolean exists(@Param("code") PackCode code);

  @Query("SELECT p FROM Pack p WHERE p.itemLotId = :itemLotId ORDER BY p.createdDate")
  Stream<PackEntity> findAllBy(@Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT new pico.erp.warehouse.pack.PackRepository$ItemLotQuantity(p.itemLotId, SUM(p.quantity)) FROM Pack p WHERE p.itemId = :itemId GROUP BY p.itemLotId")
  Stream<ItemLotQuantity> findItemLotQuantityAllBy(@Param("itemId") ItemId itemId);

}

@Repository
@Transactional
public class PackRepositoryJpa implements PackRepository {

  @Autowired
  private PackEntityRepository repository;

  @Autowired
  private PackMapper mapper;

  @Override
  public long countCreatedBetween(LocalDateTime begin, LocalDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public Pack create(@NotNull Pack bay) {
    val entity = mapper.jpa(bay);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull PackId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(@NotNull PackCode locationCode) {
    return repository.exists(locationCode);
  }

  @Override
  public boolean exists(@NotNull PackId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<Pack> findAllBy(ItemLotId itemLotId) {
    return repository.findAllBy(itemLotId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Pack> findBy(@NotNull PackId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public Stream<ItemLotQuantity> findItemLotQuantityAllBy(ItemId itemId) {
    return repository.findItemLotQuantityAllBy(itemId);
  }

  @Override
  public void update(@NotNull Pack rack) {
    val entity = repository.findById(rack.getId()).get();
    mapper.pass(mapper.jpa(rack), entity);
    repository.save(entity);
  }

}
