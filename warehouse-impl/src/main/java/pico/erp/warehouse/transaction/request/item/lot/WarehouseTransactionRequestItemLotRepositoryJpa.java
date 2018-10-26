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

  @Query("SELECT CASE WHEN COUNT(wtril) > 0 THEN true ELSE false END FROM WarehouseTransactionRequestItemLot wtril WHERE wtril.transactionRequestItemId = :transactionRequestItemId AND wtril.itemLotId = :itemLotId")
  boolean exists(
    @Param("transactionRequestItemId") WarehouseTransactionRequestItemId transactionRequestItemId,
    @Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT wtril FROM WarehouseTransactionRequestItemLot wtril WHERE wtril.transactionRequestItemId = :transactionRequestItemId")
  Stream<WarehouseTransactionRequestItemLotEntity> findAllBy(
    @Param("transactionRequestItemId") WarehouseTransactionRequestItemId transactionRequestItemId);

  @Query("SELECT wtril FROM WarehouseTransactionRequestItemLot wtril WHERE wtril.transactionRequestId = :transactionRequestId")
  Stream<WarehouseTransactionRequestItemLotEntity> findAllBy(
    @Param("transactionRequestId") WarehouseTransactionRequestId transactionRequestId);

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
  public boolean exists(WarehouseTransactionRequestItemId transactionRequestItemId,
    ItemLotId itemLotId) {
    return repository.exists(transactionRequestItemId, itemLotId);
  }

  @Override
  public Stream<WarehouseTransactionRequestItemLot> findAllBy(
    WarehouseTransactionRequestItemId transactionRequestItemId) {
    return repository.findAllBy(transactionRequestItemId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<WarehouseTransactionRequestItemLot> findAllBy(
    WarehouseTransactionRequestId transactionRequestId) {
    return repository.findAllBy(transactionRequestId)
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
