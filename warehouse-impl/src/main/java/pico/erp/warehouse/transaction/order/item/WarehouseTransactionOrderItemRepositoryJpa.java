package pico.erp.warehouse.transaction.order.item;

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
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;


@Repository
interface WarehouseTransactionOrderItemEntityRepository extends
  CrudRepository<WarehouseTransactionOrderItemEntity, WarehouseTransactionOrderItemId> {

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM WarehouseTransactionOrderItem i WHERE i.orderId = :orderId AND i.itemId = :itemId")
  boolean exists(@Param("orderId") WarehouseTransactionOrderId orderId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT i FROM WarehouseTransactionOrderItem i WHERE i.orderId = :orderId")
  Stream<WarehouseTransactionOrderItemEntity> findAllBy(
    @Param("orderId") WarehouseTransactionOrderId orderId);

}

@Repository
@Transactional
public class WarehouseTransactionOrderItemRepositoryJpa implements
  WarehouseTransactionOrderItemRepository {

  @Autowired
  private WarehouseTransactionOrderItemEntityRepository repository;

  @Autowired
  private WarehouseTransactionOrderItemMapper mapper;

  @Override
  public WarehouseTransactionOrderItem create(@NotNull WarehouseTransactionOrderItem domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionOrderItemId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionOrderItemId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionOrderId transactionOrderId, ItemId itemId) {
    return repository.exists(transactionOrderId, itemId);
  }

  @Override
  public Stream<WarehouseTransactionOrderItem> findAllBy(
    WarehouseTransactionOrderId transactionOrderId) {
    return repository
      .findAllBy(transactionOrderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionOrderItem> findBy(
    @NotNull WarehouseTransactionOrderItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionOrderItem domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
