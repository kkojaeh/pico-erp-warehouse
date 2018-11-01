package pico.erp.warehouse.transaction.order.pack;

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
interface WarehouseTransactionOrderPackEntityRepository extends
  CrudRepository<WarehouseTransactionOrderPackEntity, WarehouseTransactionOrderPackId> {

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM WarehouseTransactionOrderPack p WHERE p.orderId = :orderId AND p.itemId = :itemId")
  boolean exists(@Param("orderId") WarehouseTransactionOrderId orderId,
    @Param("itemId") ItemId itemId);

  @Query("SELECT p FROM WarehouseTransactionOrderPack p WHERE p.orderId = :orderId")
  Stream<WarehouseTransactionOrderPackEntity> findAllBy(
    @Param("orderId") WarehouseTransactionOrderId orderId);

}

@Repository
@Transactional
public class WarehouseTransactionOrderPackRepositoryJpa implements
  WarehouseTransactionOrderPackRepository {

  @Autowired
  private WarehouseTransactionOrderPackEntityRepository repository;

  @Autowired
  private WarehouseTransactionOrderPackMapper mapper;

  @Override
  public WarehouseTransactionOrderPack create(@NotNull WarehouseTransactionOrderPack domain) {
    val entity = mapper.jpa(domain);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(@NotNull WarehouseTransactionOrderPackId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(@NotNull WarehouseTransactionOrderPackId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(WarehouseTransactionOrderId orderId, ItemId itemId) {
    return repository.exists(orderId, itemId);
  }

  @Override
  public Stream<WarehouseTransactionOrderPack> findAllBy(
    WarehouseTransactionOrderId orderId) {
    return repository
      .findAllBy(orderId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<WarehouseTransactionOrderPack> findBy(
    @NotNull WarehouseTransactionOrderPackId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(@NotNull WarehouseTransactionOrderPack domain) {
    val entity = repository.findOne(domain.getId());
    mapper.pass(mapper.jpa(domain), entity);
    repository.save(entity);
  }

}
