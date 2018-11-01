package pico.erp.warehouse.transaction.request.item.lot;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemId;


@Repository
interface WarehouseTransactionRequestItemLotEntityRepository extends
  CrudRepository<WarehouseTransactionRequestItemLotEntity, WarehouseTransactionRequestItemLotId> {

  @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM WarehouseTransactionRequestItemLot l WHERE l.requestItemId = :requestItemId AND l.itemLotId = :itemLotId")
  boolean exists(
    @Param("requestItemId") WarehouseTransactionRequestItemId requestItemId,
    @Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT l FROM WarehouseTransactionRequestItemLot l WHERE l.requestItemId = :requestItemId")
  Stream<WarehouseTransactionRequestItemLotEntity> findAllBy(
    @Param("requestItemId") WarehouseTransactionRequestItemId requestItemId);

  @Query("SELECT l FROM WarehouseTransactionRequestItemLot l WHERE l.requestId = :requestId")
  Stream<WarehouseTransactionRequestItemLotEntity> findAllBy(
    @Param("requestId") WarehouseTransactionRequestId requestId);

}

@Repository
@Transactional
public class WarehouseTransactionRequestItemLotRepositoryJpa implements
  WarehouseTransactionRequestItemLotRepository {

  @Autowired
  private WarehouseTransactionRequestItemLotEntityRepository repository;

  @Autowired
  private WarehouseTransactionRequestItemLotMapper mapper;

  @Override
  public WarehouseTransactionRequestItemLot create(
    @NotNull WarehouseTransactionRequestItemLot domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionRequestItemLotId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionRequestItemLotId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionRequestItemId requestItemId,
    ItemLotId itemLotId) {
    return repository.exists(requestItemId, itemLotId);
  }

  @Override
  public Stream<WarehouseTransactionRequestItemLot> findAllBy(
    WarehouseTransactionRequestItemId requestItemId) {
    return repository.findAllBy(requestItemId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<WarehouseTransactionRequestItemLot> findAllBy(
    WarehouseTransactionRequestId requestId) {
    return repository.findAllBy(requestId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionRequestItemLot> findBy(
    @NotNull WarehouseTransactionRequestItemLotId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionRequestItemLot domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
