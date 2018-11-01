package pico.erp.warehouse.transaction.order.item.lot;

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
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemId;


@Repository
interface WarehouseTransactionOrderItemLotEntityRepository extends
  CrudRepository<WarehouseTransactionOrderItemLotEntity, WarehouseTransactionOrderItemLotId> {

  @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM WarehouseTransactionOrderItemLot l WHERE l.orderItemId = :orderItemId AND l.itemLotId = :itemLotId")
  boolean exists(
    @Param("orderItemId") WarehouseTransactionOrderItemId orderItemId,
    @Param("itemLotId") ItemLotId itemLotId);

  @Query("SELECT l FROM WarehouseTransactionOrderItemLot l WHERE l.orderItemId = :orderItemId")
  Stream<WarehouseTransactionOrderItemLotEntity> findAllBy(
    @Param("orderItemId") WarehouseTransactionOrderItemId orderItemId);

  @Query("SELECT l FROM WarehouseTransactionOrderItemLot l WHERE l.orderId = :orderId")
  Stream<WarehouseTransactionOrderItemLotEntity> findAllBy(
    @Param("orderId") WarehouseTransactionOrderId orderId);

}

@Repository
@Transactional
public class WarehouseTransactionOrderItemLotRepositoryJpa implements
  WarehouseTransactionOrderItemLotRepository {

  @Autowired
  private WarehouseTransactionOrderItemLotEntityRepository repository;

  @Autowired
  private WarehouseTransactionOrderItemLotMapper mapper;

  @Override
  public WarehouseTransactionOrderItemLot create(
    @NotNull WarehouseTransactionOrderItemLot domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionOrderItemLotId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionOrderItemLotId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionOrderItemId transactionOrderItemId,
    ItemLotId itemLotId) {
    return repository.exists(transactionOrderItemId, itemLotId);
  }

  @Override
  public Stream<WarehouseTransactionOrderItemLot> findAllBy(
    WarehouseTransactionOrderItemId transactionOrderItemId) {
    return repository.findAllBy(transactionOrderItemId)
      .map(mapper::jpa);
  }

  @Override
  public Stream<WarehouseTransactionOrderItemLot> findAllBy(
    WarehouseTransactionOrderId transactionOrderId) {
    return repository.findAllBy(transactionOrderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionOrderItemLot> findBy(
    @NotNull WarehouseTransactionOrderItemLotId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionOrderItemLot domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
